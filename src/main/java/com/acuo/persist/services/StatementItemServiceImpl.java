package com.acuo.persist.services;

import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.Step;
import com.acuo.persist.entity.enums.StatementStatus;
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
    public <T extends StatementItem> T setStatus(String statementItemId, StatementStatus status) {
        StatementItem item = find(statementItemId, 2);
        Step first = item.getFirstStep();
        Step previous = item.getLastStep();
        if (first == null || previous == null) {
            Step step = stepService.create(status);
            item.setFirstStep(step);
            item.setLastStep(step);
            item = save(item, 2);
        } else if (!status.equals(previous.getStatus())) {
            Step last = stepService.create(status);
            nextService.createNext(previous, last);
            item.setLastStep(last);
            item = save(item, 2);
        }
        return (T) item;
    }
}