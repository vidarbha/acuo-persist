package com.acuo.persist.services;

import com.acuo.common.model.results.MSError;
import com.acuo.persist.entity.Error;
import com.acuo.persist.entity.Step;
import com.acuo.persist.entity.enums.StatementStatus;
import com.google.inject.persist.Transactional;

import java.time.LocalDateTime;

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

    @Override
    @Transactional
    public Step create(StatementStatus status, MSError msError) {
        if (msError == null) {
            return create(status);
        }
        Error error = new Error();
        error.setErrorCode(msError.getErrorCode());
        error.setErrorDesc(msError.getErrorDescription());
        error.setErrorMsg(msError.getErrorMessage());
        error.setStatusCode(msError.getStatusCode());
        error.setStatusDesc(msError.getHttpStatusDescription());
        error.setDateTime(LocalDateTime.now());
        error.setStatus(StatementStatus.Error);
        error.setStatusAimed(status);
        return save(error);
    }
}
