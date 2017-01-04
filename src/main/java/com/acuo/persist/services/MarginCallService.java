package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.MarginCall;

public interface MarginCallService extends Service<MarginCall>  {

    void setStatus(String marginCallId, CallStatus status);
}
