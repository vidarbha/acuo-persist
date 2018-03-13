package com.acuo.persist.services;

import com.acuo.common.model.results.MSError;
import com.acuo.persist.entity.Step;
import com.acuo.persist.entity.StepError;
import com.acuo.persist.entity.enums.StatementStatus;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.LocalDateTime;

public class StepServiceImpl extends AbstractService<Step, String> implements StepService {

    @Inject
    public StepServiceImpl(Provider<Session> session) {
        super(session);
    }

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
        StepError stepError = new StepError();
        stepError.setErrorCode(msError.getErrorCode());
        stepError.setErrorDesc(msError.getErrorDescription());
        stepError.setErrorMsg(msError.getErrorMessage());
        stepError.setStatusCode(msError.getStatusCode());
        stepError.setStatusDesc(msError.getHttpStatusDescription());
        stepError.setDateTime(LocalDateTime.now());
        stepError.setStatus(StatementStatus.Error);
        stepError.setStatusAimed(status);
        return save(stepError);
    }
}
