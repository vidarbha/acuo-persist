package com.acuo.persist.services;

import com.acuo.persist.entity.Step;
import com.acuo.persist.entity.enums.StatementStatus;
import com.google.inject.persist.Transactional;

public class StepServiceImpl extends GenericService<Step, String> implements StepService {

    @Override
    public Class<Step> getEntityType() {
        return Step.class;
    }

    @Override
    @Transactional
    public Step create(StatementStatus status) {
        Step step = new Step();
        step.setStatus(status);
        return save(step);
    }
}
