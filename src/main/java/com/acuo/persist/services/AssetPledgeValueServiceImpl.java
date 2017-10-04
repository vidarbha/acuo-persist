package com.acuo.persist.services;

import com.acuo.persist.entity.AssetPledgeValue;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.Instant;
import java.time.LocalDate;

public class AssetPledgeValueServiceImpl extends AbstractService<AssetPledgeValue, Long> implements AssetPledgeValueService {

    @Inject
    public AssetPledgeValueServiceImpl(Provider<Session> session) {
        super(session);
    }

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
