package com.acuo.persist.spring;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@Ignore
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        RepositoryModule.class,
        SpringGuiceModule.class
})
public class ClientRepositoryTest {

    @Inject
    ClientRepository clientRepository;

    @Test
    public void test() {
        clientRepository.allCallsFor("999", "25/10/16 12:00");
    }
}
