package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.Next;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.Step;

public class StatementItemServiceImpl extends GenericService<StatementItem> implements StatementItemService {

    @Override
    public Class<StatementItem> getEntityType() {
        return StatementItem.class;
    }

    public  void setStatus(StatementItem statementItem, CallStatus status)
    {
        statementItem = find(statementItem.getId());
        Step previousStep = statementItem.getLastStep();
        Step lastStep = new Step();
        Next next = new Next();
        next.setStart(previousStep);
        next.setEnd(lastStep);
        previousStep.setNext(next);
        lastStep.setStatus(status);
        statementItem.setLastStep(lastStep);
        save(statementItem);
    }
}
