package com.acuo.persist.services;

import com.acuo.persist.entity.CollateralValue;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Provider;
import java.time.Instant;
import java.time.LocalDate;

public class CollateralValueServiceImpl extends AbstractService<CollateralValue, Long> implements CollateralValueService {

    @Inject
    public CollateralValueServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<CollateralValue> getEntityType() {
        return CollateralValue.class;
    }


    @Override
    @Transactional
    public CollateralValue createValue(Double amount) {
        CollateralValue value = new CollateralValue();
        value.setAmount(amount);
        value.setTimestamp(Instant.now());
        value.setValuationDate(LocalDate.now());
        return save(value);
    }
}
