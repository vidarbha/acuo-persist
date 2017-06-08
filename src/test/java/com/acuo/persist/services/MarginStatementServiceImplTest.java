package com.acuo.persist.services;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.Step;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.MarginStatementId;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.DataImporterModule;
import com.acuo.persist.modules.DataLoaderModule;
import com.acuo.persist.modules.Neo4jPersistModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.helpers.collection.Iterables;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        Neo4jPersistModule.class,
        DataLoaderModule.class,
        DataImporterModule.class,
        RepositoryModule.class})
public class MarginStatementServiceImplTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private MarginStatementService marginStatementService = null;

    private ClientId client999 = ClientId.fromString("999");

    @Before
    public void setUp() throws Exception {
        importService.reload();
    }

    @Test
    public void testAllStatementsForClient() {
        Iterable<MarginStatement> statements = marginStatementService.allStatementsForClient(client999);
        assertThat(statements).isNotNull().hasSize(42);
    }

    @Test
    public void testAllStatementsFor() {
        Iterable<MarginStatement> statements = marginStatementService.allStatementsFor(client999, StatementStatus.Reconciled);
        assertThat(statements).isNotNull().hasSize(2);
        assertThat(statements).flatExtracting(MarginStatement::getStatementItems)
                .allSatisfy(statementItem -> assertThat(statementItem.getLastStep())
                        .extracting(Step::getStatus)
                        .containsOnly(StatementStatus.Reconciled));
    }

    @Test
    public void testAllStatementsForRecon() {
        Iterable<MarginStatement> statements = marginStatementService.allStatementsForRecon(client999);
        assertThat(statements).isNotNull().hasSize(42);
        assertThat(Iterables.first(statements).getPendingCash()).isNotNull();
    }

    @Test
    public void testStatementFor(){
        MarginStatement msp1 = marginStatementService.statementFor(MarginStatementId.fromString("msp1"), StatementStatus.Expected);
        assertThat(msp1).isNotNull();
        assertThat(msp1.getAgreement()).isNotNull();
        assertThat(msp1.getStatementItems()).isNotNull()
                .extracting(StatementItem::getLastStep)
                .extracting(Step::getStatus)
                .containsOnly(StatementStatus.Expected);

        MarginStatement msp31 = marginStatementService.statementFor(MarginStatementId.fromString("msp31"), StatementStatus.Reconciled);
        assertThat(msp31).isNotNull();
        assertThat(msp1.getAgreement()).isNotNull();
        assertThat(msp31.getStatementItems()).isNotNull()
                .extracting(StatementItem::getLastStep)
                .extracting(Step::getStatus)
                .containsOnly(StatementStatus.Reconciled);

    }

    @Test
    public void testAllUnmatchedStatements(){
        Iterable<MarginStatement> marginStatements = marginStatementService.allUnmatchedStatements(client999);
        assertThat(marginStatements).isNotNull();

    }

    @Test
    public void testStatementForRecon(){
        Iterable<MarginStatement> statements = marginStatementService.allStatementsForRecon(client999);
        assertThat(statements).isNotNull().hasSize(42);
        assertThat(statements).flatExtracting(MarginStatement::getStatementItems)
                .allSatisfy(statementItem -> assertThat(statementItem)
                        .extracting("lastStep.status")
                        .isSubsetOf(StatementStatus.Unrecon, StatementStatus.Expected));
    }

    @Test
    public void testStatementOf(){
        MarginStatement mcp1 = marginStatementService.statementOf("mcp1");
        assertThat(mcp1).isNotNull();
    }

    @Test
    @Ignore
    public void testReconcile(){
        MarginStatement msp1 = marginStatementService.statementFor(MarginStatementId.fromString("msp1"), StatementStatus.Unrecon);
        assertThat(msp1).isNotNull();
        assertThat(msp1.getAgreement()).isNotNull();
        assertThat(msp1.getStatementItems()).isNotNull()
                .hasSize(2)
                .extracting(StatementItem::getLastStep)
                .extracting(Step::getStatus)
                .containsOnly(StatementStatus.Expected);
    }

    @Test
    @Ignore
    public void testMatch(){

    }

    @Test
    @Ignore
    public void testGetMarginStatement(){

    }

    @Test
    @Ignore
    public void testGetOrCreateMarginStatement(){

    }

    @Test
    @Ignore
    public void testSetStatus(){

    }

    @Test
    @Ignore
    public void testGetCountForMenu(){

    }
}