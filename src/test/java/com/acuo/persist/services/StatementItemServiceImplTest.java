package com.acuo.persist.services;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.entity.InterestPayment;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.DataImporterModule;
import com.acuo.persist.modules.DataLoaderModule;
import com.acuo.persist.modules.Neo4jPersistModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        Neo4jPersistModule.class,
        DataLoaderModule.class,
        DataImporterModule.class,
        RepositoryModule.class})
public class StatementItemServiceImplTest {

    @Inject
    StatementItemService statementItemService;

    @Before
    public void setUp() {
        InterestPayment interestPayment = new InterestPayment();
        interestPayment.setItemId("id");
        statementItemService.save(interestPayment);
    }

    @Test
    public void testStatus() {
        StatementItem id = statementItemService.setStatus("id", StatementStatus.Expected);
        assertThat(id).isNotNull();
        assertThat(id.getLastStep()).isNotNull();
        assertThat(id.getLastStep().getStatus()).isEqualTo(StatementStatus.Expected);
        id = statementItemService.setStatus("id", StatementStatus.Pledged);
        assertThat(id).isNotNull();
        assertThat(id.getLastStep()).isNotNull();
        assertThat(id.getLastStep().getStatus()).isEqualTo(StatementStatus.Pledged);
    }
}