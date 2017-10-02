package com.acuo.persist.services;

import com.acuo.persist.entity.AssetPledgeValue;

public interface AssetPledgeValueService extends Service<AssetPledgeValue, Long> {

    AssetPledgeValue createValue(Double amount);
}
