package com.acuo.persist.services;

import com.acuo.persist.entity.Step;
import com.google.inject.persist.Transactional;

public class StepServiceImpl extends GenericService<Step, String> implements StepService {

    @Override
    public Class<Step> getEntityType() {
        return Step.class;
    }

    @Override
    @Transactional
    public Step create() {
        Step step = new Step();
        return save(step);
    }
}
