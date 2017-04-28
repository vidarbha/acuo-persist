package com.acuo.persist.services;

import com.acuo.persist.entity.Next;
import com.acuo.persist.entity.Step;

public interface NextService extends Service<Next, String> {
    Next createNext(Step previousStep, Step lastStep);
}
