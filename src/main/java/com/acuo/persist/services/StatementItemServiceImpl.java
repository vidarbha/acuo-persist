package com.acuo.persist.services;

import com.acuo.common.model.results.MSError;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.Step;
import com.acuo.persist.entity.enums.StatementStatus;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Provider;

public class StatementItemServiceImpl extends AbstractService<StatementItem, String> implements StatementItemService {

    private final NextService nextService;
    private final StepService stepService;

    @Inject
    public StatementItemServiceImpl(Provider<Session> session,
                                    NextService nextService,
                                    StepService stepService) {
        super(session);
        this.nextService = nextService;
        this.stepService = stepService;
    }

    @Override
    public Class<StatementItem> getEntityType() {
        return StatementItem.class;
    }

    @Override
    @Transactional
    public <T extends StatementItem> T setStatus(String statementItemId, StatementStatus status) {
        return setStatus(statementItemId, status, null);
    }

    @Override
    @Transactional
    public <T extends StatementItem> T setStatus(String statementItemId, StatementStatus status, MSError msError) {
        StatementItem item = find(statementItemId, 2);
        if (item == null) {
            return null;
        }
        Step first = item.getFirstStep();
        Step previous = item.getLastStep();
        if (first == null || previous == null) {
            Step step = stepService.create(status, msError);
            item.setFirstStep(step);
            item.setLastStep(step);
            item = save(item, 2);
        } else if (msError != null || !status.equals(previous.getStatus())) {
            Step last = stepService.create(status, msError);
            nextService.createNext(previous, last);
            item.setLastStep(last);
            item = save(item, 2);
        }
        return (T) item;
    }
}