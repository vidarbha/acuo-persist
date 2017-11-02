package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.model.results.AssetSettlementDate;
import com.acuo.persist.entity.ServiceError;
import com.acuo.persist.entity.Settlement;
import com.acuo.persist.entity.SettlementDate;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
public class SettlementServiceImpl extends AbstractService<Settlement, String> implements SettlementService{

    private final AssetService assetService;
    private final SettlementDateService settlementDateService;
    private final ErrorService errorService;

    @Inject
    public SettlementServiceImpl(Provider<Session> session,
                                 AssetService assetService,
                                 SettlementDateService settlementDateService,
                                 ErrorService errorService) {
        super(session);
        this.assetService = assetService;
        this.settlementDateService = settlementDateService;
        this.errorService = errorService;
    }

    @Override
    public Class<Settlement> getEntityType() {
        return Settlement.class;
    }

    @Override
    public Settlement getSettlementFor(AssetId assetId) {
        String query =
                "MATCH p=()<-[:SETTLEMENT_DATE|LATEST*0..1]-(sd:Settlement)<-[:SETTLEMENT]-(asset:Asset {id:{id}}) " +
                "RETURN sd, nodes(p), relationships(p)";
        ImmutableMap<String, String> parameters = ImmutableMap.of("id", assetId.toString());
        return dao.getSession().queryForObject(Settlement.class, query, parameters);
    }

    @Override
    public Settlement getOrCreateSettlementFor(AssetId assetId) {
        Settlement settlement = getSettlementFor(assetId);
        if (settlement == null) {
            settlement = new Settlement();
            settlement.setSettlementId(assetId.toString() + "-settlement");
            settlement.setAsset(assetService.find(assetId));
            save(settlement);
        }
        return settlement;
    }

    @Override
    @Transactional
    public Settlement persist(AssetSettlementDate settlementDate) {
        AssetId assetId = AssetId.fromString(settlementDate.getAssetId());
        LocalDate date = settlementDate.getSettlementDate().plusDays(1);

        Settlement settlement = getOrCreateSettlementFor(assetId);

        final SettlementDate child = settlementDateService.createSettlementDate(date);
        child.setSettlement(settlement);
        settlement.setLatestSettlementDate(child);

        return save(settlement);
    }

    @Override
    @Transactional
    public void persist(List<AssetSettlementDate> assetSettlementDates, Map<String, List<ServiceError>> errors) {

        if(assetSettlementDates != null) {
            log.info("Persisting the {} settlement dates", assetSettlementDates.size());
            assetSettlementDates.forEach(this::persist);
        }

        if (errors != null) {
            log.info("persisting the errors that exist for settlement dates");
            errors.forEach((key, value) -> errorService.persist(AssetId.fromString(key), value));
        }
    }
}