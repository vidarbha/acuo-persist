package com.acuo.persist.services;

import com.acuo.common.model.results.MSError;
import com.acuo.persist.entity.Step;
import com.acuo.persist.entity.enums.StatementStatus;

public interface StepService extends Service<Step, String> {

    Step create(StatementStatus status);

    Step create(StatementStatus status, MSError error);

}