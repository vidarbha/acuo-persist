package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.model.results.AssetSettlementDate;
import com.acuo.persist.entity.ServiceError;
import com.acuo.persist.entity.Settlement;

import java.util.List;
import java.util.Map;

public interface SettlementService extends Service<Settlement, String> {

    Settlement getSettlementFor(AssetId assetId);

    Settlement getOrCreateSettlementFor(AssetId assetId);

    Settlement persist(AssetSettlementDate settlementDate);

    void persist(List<AssetSettlementDate> assetSettlementDates, Map<String, List<ServiceError>> errors);
}
