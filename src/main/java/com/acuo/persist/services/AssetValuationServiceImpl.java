package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.AssetValue;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static com.acuo.common.util.ArgChecker.notNull;
import static java.util.stream.Collectors.toList;

@Slf4j
public class AssetValuationServiceImpl extends AbstractService<AssetValuation, Long> implements AssetValuationService {

    private final AssetService assetService;
    private final ValueService valueService;

    private static final Predicate<AssetValue> assetValuePredicate = assetValue -> assetValue.getValuationDate()
                                                                                   .isBefore(LocalDate.now().minusDays(5));

    @Inject
    public AssetValuationServiceImpl(Provider<Session> session,
                                     AssetService assetService,
                                     ValueService valueService) {
        super(session);
        this.assetService = assetService;
        this.valueService = valueService;
    }

    @Override
    public Class<AssetValuation> getEntityType() {
        return AssetValuation.class;
    }

    @Override
    @Transactional
    public AssetValuation getAssetValuationFor(AssetId assetId) {
        String query =
                "MATCH p=(value:AssetValue)<-[*0..1]-(valuation:AssetValuation)-[:VALUATED]->(asset:Asset {id:{id}})-[*0..1]-(n) " +
                        "RETURN p, nodes(p), relationships(p)";
        final String aId = assetId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", aId);
        return dao.getSession().queryForObject(AssetValuation.class, query, parameters);
    }

    @Override
    @Transactional
    public AssetValuation getOrCreateAssetValuationFor(AssetId assetId) {
        AssetValuation valuation = getAssetValuationFor(assetId);
        if (valuation == null) {
            valuation = new AssetValuation();
            Asset asset = assetService.find(assetId);
            valuation.setAsset(notNull(asset, "asset"));
            valuation = save(valuation, 1);
        }
        return valuation;
    }

    public Collection<AssetValue> persist(List<com.acuo.common.model.results.AssetValuation> valuations) {
        return valuations.stream().map(this::persist).collect(toList());
    }

    public AssetValue persist(com.acuo.common.model.results.AssetValuation valuation) {
        final AssetId assetId = AssetId.fromString(valuation.getAssetId());
        if (log.isDebugEnabled()) {
            log.debug("inserting asset valuation for asset id [{}]", assetId);
        }
        final LocalDateTime valuationDateTime = valuation.getValuationDateTime();
        AssetValue assetValue = createAssetValue(valuation, valuationDateTime);

        assetValue = persist(assetId, assetValue);

        if (log.isDebugEnabled()) {
            log.debug("valuation inserted in the db with valuation date set to {} and timestamp to {}",
                    assetValue.getValuationDate(), assetValue.getTimestamp());
        }
        return assetValue;
    }

    @Override
    @Transactional
    public AssetValue persist(AssetId assetId, AssetValue value) {

        AssetValuation assetValuation = getOrCreateAssetValuationFor(assetId);

        deleteOldestValues(assetValuation);

        assetValuation.setLatestValue(value);
        value.setValuation(assetValuation);
        save(assetValuation, 2);

        return valueService.save(value, 2);
    }

    @Override
    public Optional<AssetValue> latest(AssetId assetId) {
        AssetValuation assetValuation = getAssetValuationFor(assetId);
        return (assetValuation != null) ? Optional.ofNullable(assetValuation.getLatestValue()) : Optional.empty();
    }

    private AssetValue createAssetValue(com.acuo.common.model.results.AssetValuation valuation, LocalDateTime valuationDateTime) {
        AssetValue assetValue = new AssetValue();
        assetValue.setCoupon(valuation.getCoupon());
        assetValue.setNominalCurrency(valuation.getNominalCurrency());
        assetValue.setUnitValue(valuation.getCleanMarketValue());
        assetValue.setPriceQuotationType(valuation.getPriceQuotationType());
        assetValue.setReportCurrency(valuation.getReportCurrency());
        assetValue.setValuationDate(valuationDateTime.toLocalDate());
        assetValue.setTimestamp(valuationDateTime.toInstant(ZoneOffset.UTC));
        assetValue.setYield(valuation.getYield());
        return valueService.save(assetValue);
    }

    private void deleteOldestValues(AssetValuation assetValuation) {
        final Set<AssetValue> values = assetValuation.getValues();
        if (values == null) return;
        final List<AssetValue> toDelete = values.stream()
                .filter(assetValuePredicate)
                .collect(toList());
        if(values.removeAll(toDelete)) {
            assetValuation.setValues(values);
        }
        if(!toDelete.isEmpty()) {
            valueService.delete(toDelete);
        }
    }
}