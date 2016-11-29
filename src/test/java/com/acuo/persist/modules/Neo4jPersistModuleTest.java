package com.acuo.persist.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class Neo4jPersistModuleTest {

    AtomicInteger count = new AtomicInteger(0);

    interface HotDog {}

    static class NathansDog implements HotDog {}

    interface Store {
        HotDog cookDog();
    }

    static class ConeyIslandStore implements Store {
        public NathansDog cookDog() {
            return null;
        }
    }

    private class CountingInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            count.incrementAndGet();
            return null;
        }
    }

    @Test
    public void testDoesNotInterceptTwiceOnSyntheticMethods() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Store.class).to(ConeyIslandStore.class);
                bindInterceptor(Matchers.any(), new AbstractMatcher<Method>() {
                    public boolean matches(Method t) {
                        System.out.println("synthetic: " + t.isSynthetic() + ", t: " + t);
                        return true;
                    }
                }, new CountingInterceptor());
            }
        });
        Store store = injector.getInstance(Store.class);
        store.cookDog();
        assertEquals(1, count.get());
    }
}
