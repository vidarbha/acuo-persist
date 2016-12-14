package com.acuo.persist.services;

import com.acuo.persist.entity.IRS;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

import javax.inject.Singleton;

@Singleton
@Transactional
public class IRSServiceImpl extends GenericService<IRS> implements IRSService {
    @Override
    public Class<IRS> getEntityType() {
        return IRS.class;
    }

    @Override
    public IRS findById(String id) {
        String query = "match (i:IRS {id: {id} }) return i";
        return session.queryForObject(IRS.class, query, ImmutableMap.of("id",id));
    }
}
