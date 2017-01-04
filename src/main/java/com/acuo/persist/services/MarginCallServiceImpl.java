package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.Next;
import com.acuo.persist.entity.Step;

public class MarginCallServiceImpl extends GenericService<MarginCall> implements MarginCallService {

    @Override
    public Class<MarginCall> getEntityType() {
        return MarginCall.class;
    }

    @Override
    public void setStatus(String marginCallId, CallStatus status)
    {
        MarginCall marginCall = this.findById(marginCallId, 1);
        Step previousStep = marginCall.getLastStep();
        Step lastStep = new Step();
        Next next = new Next();
        next.setStart(previousStep);
        next.setEnd(lastStep);
        previousStep.setNext(next);
        lastStep.setStatus(status);
        marginCall.setLastStep(lastStep);
        this.createOrUpdate(marginCall);
    }
}
