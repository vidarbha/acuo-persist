package com.acuo.persist.services;

import com.acuo.persist.entity.Counterpart;
import com.acuo.persist.entity.LegalEntity;

public interface CounterpartService extends Service<Counterpart> {

    Counterpart getCounterpart(LegalEntity legalEntity);
}
