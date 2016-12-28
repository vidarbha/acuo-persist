package com.acuo.persist.services;

import com.acuo.persist.spring.Call;
import com.acuo.persist.entity.MarginStatement;

public interface MarginStatementService extends Service<MarginStatement> {

    Iterable<Call> allCallsFor(String clientId, String dateTime);

}
