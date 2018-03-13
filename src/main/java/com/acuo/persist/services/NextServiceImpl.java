package com.acuo.persist.services;

import com.acuo.persist.entity.Next;
import com.acuo.persist.entity.Step;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.LocalDateTime;

public class NextServiceImpl extends AbstractService<Next, String> implements NextService {

    @Inject
    public NextServiceImpl(Provider<Session> session) {
        super(session);
    }

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
        previous.setNext(next);
        return save(next);
    }
}
