package com.acuo.persist.learning;

import com.googlecode.junittoolbox.MultithreadingTester;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestConcurrentDelete {

    @Test
    public void test_with_long_running_worker() {
        new MultithreadingTester().numThreads(2).numRoundsPerThread(1).add(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(1000);
                return null;
            }
        }).run();
    }


}
