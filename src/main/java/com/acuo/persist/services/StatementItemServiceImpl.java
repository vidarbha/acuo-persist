package com.acuo.persist.services;

import com.acuo.persist.entity.Next;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.Step;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class StatementItemServiceImpl extends GenericService<StatementItem, Long> implements StatementItemService {

    private final NextService nextService;
    private final StepService stepService;

    @Inject
    public StatementItemServiceImpl(NextService nextService, StepService stepService) {
        this.nextService = nextService;
        this.stepService = stepService;
    }

    @Override
    public Class<StatementItem> getEntityType() {
        return StatementItem.class;
    }

    @Override
    @Transactional
    public void setStatus(StatementItem statementItem, StatementStatus status) {
        statementItem = find(statementItem.getId());
        Step previous = statementItem.getLastStep();
        Step last = stepService.create();
        Next next = nextService.createNext(previous, last);
        previous.setNext(next);
        last.setStatus(status);
        statementItem.setLastStep(last);
        save(statementItem, 1);
    }
}