package com.acuo.persist;

import com.google.inject.persist.Transactional;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * @author cgdecker@gmail.com (Colin Decker)
 */
public class TransactionTest extends AbstractPersistTest {

  @Ignore
  @Test public void unitOfWorkPerTransactionIfNotStartedManually() {
    TransactionConnectionGetter transactionalObj = injector.getInstance(TransactionConnectionGetter.class);
    Session session =  transactionalObj.getConnection();
    Session session2 = transactionalObj.getConnection();
    assertThat(session).isNotNull();
    assertThat(session2).isNotNull();
    assertFalse("conn from first transactional call same as conn from second", session == session2);
  }

  @Ignore
  @Test public void unitOfWorkSharedIfStartedManually() {
    TransactionConnectionGetter transactionalObj = injector.getInstance(TransactionConnectionGetter.class);
    getUnitOfWork().begin();
    Session session =  transactionalObj.getConnection();
    Session session2 = transactionalObj.getConnection();
    getUnitOfWork().end();
    assertThat(session).isNotNull();
    assertThat(session2).isNotNull();
    assertFalse("conn from first transactional call same as conn from second", session == session2);
  }

  public static class TransactionConnectionGetter {
    private final Provider<Session> sessionProvider;

    @Inject public TransactionConnectionGetter(Provider<Session> sessionProvider) {
      this.sessionProvider = sessionProvider;
    }

    @Transactional
    public Session getConnection() {
      return sessionProvider.get();
    }
  }
}