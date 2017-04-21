package com.acuo.persist.services;

import com.acuo.persist.entity.Next;
import com.acuo.persist.entity.Step;
import com.google.inject.persist.Transactional;

import java.time.LocalDateTime;

public class NextServiceImpl extends GenericService<Next, String> implements NextService {

    @Override
    public Class<Next> getEntityType() {
        return Next.class;
    }

    @Override
    @Transactional
    public Next createNext(Step previous, Step last) {
        Next next = new Next();
        next.setTime(LocalDateTime.now());
        next.setStart(previous);
        next.setEnd(last);
        return save(next);
    }
}
