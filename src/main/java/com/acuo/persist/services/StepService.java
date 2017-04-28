package com.acuo.persist.services;

import com.acuo.persist.entity.Step;

public interface StepService extends Service<Step, String> {

    Step create();

}