package com.acuo.persist.services;

import com.acuo.persist.entity.Rule;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class RuleServiceImpl extends AbstractService<Rule, Long> implements RuleService {

    @Inject
    public RuleServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<Rule> getEntityType() {
        return Rule.class;
    }
}
