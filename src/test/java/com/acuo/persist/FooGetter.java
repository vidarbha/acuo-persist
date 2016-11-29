package com.acuo.persist;

import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import java.util.Collection;

public class FooGetter {

  public static final int DEPTH_LIST = 1;
  public static final int DEPTH_ENTITY = 1;

  private Session session;

  @Inject
  public FooGetter(Session session) {
    this.session = session;
  }

  @Transactional
  public Collection<Foo> getFoos() {
    return session.loadAll(Foo.class, DEPTH_LIST);
  }
}