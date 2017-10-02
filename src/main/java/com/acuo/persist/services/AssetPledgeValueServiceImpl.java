package com.acuo.persist.services;

import com.acuo.persist.entity.AssetPledgeValue;

import java.time.Instant;
import java.time.LocalDate;

public class AssetPledgeValueServiceImpl extends GenericService<AssetPledgeValue, Long> implements AssetPledgeValueService {

    @Override
    public Class<AssetPledgeValue> getEntityType() {
        return AssetPledgeValue.class;
    }

    @Override
    public AssetPledgeValue createValue(Double amount) {
        AssetPledgeValue value = new AssetPledgeValue();
        value.setAmount(amount);
        value.setTimestamp(Instant.now());
        value.setValuationDate(LocalDate.now());
        return save(value);
    }
}
