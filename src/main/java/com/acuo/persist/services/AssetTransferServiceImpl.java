package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.ClientId;
import com.acuo.common.util.LocalDateUtils;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.CustodianAccount;
import com.acuo.persist.entity.Holds;
import com.acuo.persist.entity.LegalEntity;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.SettlementDate;
import com.acuo.persist.entity.enums.AssetTransferStatus;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.acuo.persist.entity.enums.AssetTransferStatus.*;

public class AssetTransferServiceImpl extends AbstractService<AssetTransfer, String> implements AssetTransferService {

    private final MarginCallService marginCallService;
    private final AssetService assetService;
    private final CustodianAccountService custodianAccountService;
    private final AssetValuationService assetValuationService;
    private final FXRateService fxRateService;
    private final CollateralService collateralService;

    @Inject
    public AssetTransferServiceImpl(Provider<Session> session,
                                    MarginCallService marginCallService,
                                    AssetService assetService,
                                    CustodianAccountService custodianAccountService,
                                    AssetValuationService assetValuationService,
                                    FXRateService fxRateService,
                                    CollateralService collateralService) {
        super(session);
        this.marginCallService = marginCallService;
        this.assetService = assetService;
        this.custodianAccountService = custodianAccountService;
        this.assetValuationService = assetValuationService;
        this.fxRateService = fxRateService;
        this.collateralService = collateralService;
    }

    @Override
    public Class<AssetTransfer> getEntityType() {
        return AssetTransfer.class;
    }

    private final static String ARRIVING_ASSET_TRANSFER =
            "MATCH (client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:CLIENT_SIGNS]->(agreement:Agreement)-[:IS_COMPOSED_OF]->" +
            "(rule:Rule)-[:APPLIES_TO]->(asset:Asset) " +
            "WITH asset, client, rule " +
            "MATCH h=(:Custodian)-[:MANAGES]->(ca:CustodianAccount)-[holds:HOLDS]->(asset) " +
            "MATCH v=(asset)<-[:VALUATED]-(:AssetValuation)-[:VALUE]->(:AssetValue) " +
            "MATCH t=(asset)<-[:OF]-(transfer:AssetTransfer {status:'Arriving'})-[:TO]->(:CustodianAccount)<-[:HAS]-(client) " +
            "RETURN transfer, " +
            "nodes(h), relationships(h), " +
            "nodes(v), relationships(v), " +
            "nodes(t), relationships(t)";

    private final static String DEPARTED_ASSET_TRANSFER =
            "MATCH (client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:CLIENT_SIGNS]->(agreement:Agreement)-[:IS_COMPOSED_OF]->" +
            "(rule:Rule)-[:APPLIES_TO]->(asset:Asset) " +
            "WITH asset, client, rule " +
            "MATCH h=(:Custodian)-[:MANAGES]->(ca:CustodianAccount)-[holds:HOLDS]->(asset) " +
            "MATCH v=(asset)<-[:VALUATED]-(:AssetValuation)-[:VALUE]->(:AssetValue) " +
            "MATCH t=(asset)<-[:OF]-(transfer:AssetTransfer {status:'Departed'})-[:FROM]->(:CustodianAccount)<-[:HAS]-(client) " +
            "RETURN transfer, " +
            "nodes(h), relationships(h), " +
            "nodes(v), relationships(v), " +
            "nodes(t), relationships(t)";

    @Override
    @Transactional
    public Iterable<AssetTransfer> findArrivingAssetTransferByClientId(ClientId clientId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId.toString());
        return dao.getSession().query(getEntityType(), ARRIVING_ASSET_TRANSFER, parameters);
    }

    @Override
    @Transactional
    public Iterable<AssetTransfer> findDepartedAssetTransferByClientId(ClientId clientId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId.toString());
        return dao.getSession().query(getEntityType(), DEPARTED_ASSET_TRANSFER, parameters);
    }

    @Override
    @Transactional
    public void sendAsset(String marginCallId, AssetId assetId, Double quantity, String fromAccount) {
        MarginCall call = marginCallService.callById(marginCallId);


        AssetTransfer assetTransfer = createAssetTransfer(call, assetId, quantity, Departed, InFlight);

        CustodianAccount custodianAccount = custodianAccountService.find(fromAccount, 2);
        assetTransfer.setFrom(custodianAccount);
        final LegalEntity directedTo = call.getMarginStatement().getDirectedTo();
        assetTransfer.setTo(directedTo.getCustodianAccounts().iterator().next());
        save(assetTransfer, 1);

        removeQuantity(assetId, quantity);

        collateralService.handle(assetTransfer);
    }

    @Override
    @Transactional
    public void receiveAsset(String marginCallId, AssetId assetId, Double quantity, String toAccount) {
        MarginCall call = marginCallService.callById(marginCallId);
        AssetTransfer assetTransfer = createAssetTransfer(call, assetId, quantity, Arriving, InFlight);

        CustodianAccount custodianAccount = custodianAccountService.find(toAccount, 2);
        assetTransfer.setTo(custodianAccount);
        final LegalEntity directedTo = call.getMarginStatement().getDirectedTo();
        final Iterable<CustodianAccount> accounts = custodianAccountService.counterPartyCustodianAccountsFor(directedTo);
        assetTransfer.setFrom(accounts.iterator().next());
        save(assetTransfer, 1);

        //addQuantity(assetId, quantity);
        collateralService.handle(assetTransfer);
    }

    private AssetTransfer createAssetTransfer(MarginCall call,
                                              AssetId assetId,
                                              Double quantity,
                                              AssetTransferStatus status,
                                              AssetTransferStatus subStaus) {
        AssetTransfer assetTransfer = new AssetTransfer();
        assetTransfer.setAssertTransferId(call.getItemId() + "-" + assetId);
        assetTransfer.setQuantity(quantity);
        assetTransfer.setStatus(status);
        assetTransfer.setSubStatus(subStaus);
        assetTransfer.setPledgeTime(LocalDateTime.now());
        assetTransfer.setGeneratedBy(call);

        Asset asset = assetService.find(assetId, 2);
        assetTransfer.setOf(asset);
        if ("CASH".equals(asset.getType())) {
            LocalDate settlementDate = LocalDateUtils.adjustForWeekend(LocalDate.now());
            assetTransfer.setSettlementDate(settlementDate);
        } else {
            final Optional<SettlementDate> settlementDate = assetService.settlementDate(assetId);
            settlementDate.ifPresent(s -> assetTransfer.setSettlementDate(s.getSettlementDate()));
        }
        // UnitValue
        assetValuationService.latest(asset.getAssetId())
                .ifPresent(assetValue -> assetTransfer.setUnitValue(assetValue.getUnitValue()));

        // FXRate
        Double assetFx = fxRateService.getFXValue(asset.getCurrency());
        Double callFx = fxRateService.getFXValue(call.getCurrency());
        assetTransfer.setAssetFxRate(assetFx);
        assetTransfer.setCallFxRate(callFx);

        // totalHaircut
        Double totalHaircut = assetService.totalHaircut(assetId, call.getItemId());
        assetTransfer.setTotalHaircut(totalHaircut);

        return save(assetTransfer, 1);
    }

    private void removeQuantity(AssetId assetId, Double quantity) {
        Asset asset = assetService.find(assetId, 2);
        Holds holds = asset.getHolds();
        if (holds != null) {
            holds.setAvailableQuantity(holds.getAvailableQuantity() - quantity);
        }
        assetService.save(asset, 1);
    }

    public Result getPledgedAssets() {
        String query =
        "MATCH (asset:Asset)<-[:OF]-(at:AssetTransfer {status:'Departed'})-[:GENERATED_BY]->(call:MarginCall)\n" +
        "MATCH (call)-[:PART_OF]->(:MarginStatement)-[:STEMS_FROM]->(a:Agreement) \n" +
        "MATCH (l1:LegalEntity)-[:CLIENT_SIGNS]->(a)<-[:COUNTERPARTY_SIGNS]-(l2:LegalEntity) \n" +
        "MATCH (c1:Custodian)-[:MANAGES]->(ca1:CustodianAccount)<-[:FROM]-(at)-[:TO]->(ca2:CustodianAccount)<-[:MANAGES]-(c2:Custodian)\n" +
        "RETURN at.id, at.pledgeTime, a.name, l1.name, l2.name, a.currency, at.subStatus, c1.name, " +
        "c2.name, ca1.name, ca2.name, at.quantity, asset.currency,asset.name, asset.id, asset.settlementTime, at.settlementDate";
        return dao.getSession().query(query, Collections.emptyMap());
    }
}