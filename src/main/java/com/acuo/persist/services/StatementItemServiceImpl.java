package com.acuo.persist.services;

import com.acuo.persist.entity.Next;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.Step;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class StatementItemServiceImpl extends GenericService<StatementItem, String> implements StatementItemService {

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
    public void setStatus(String statementItemId, StatementStatus status) {
        StatementItem item = find(statementItemId, 2);
        Step firstStep = item.getFirstStep();
        Step previousStep = item.getLastStep();
        if (firstStep == null || previousStep == null) {
            Step step = new Step();
            step.setStatus(status);
            item.setFirstStep(step);
            item.setLastStep(step);
            save(item, 2);
        } else if (!status.equals(previousStep.getStatus())) {
            Step lastStep = new Step();
            Next next = new Next();
            next.setStart(previousStep);
            next.setEnd(lastStep);
            previousStep.setNext(next);
            lastStep.setStatus(status);
            item.setLastStep(lastStep);
            save(item, 2);
        }
    }
}