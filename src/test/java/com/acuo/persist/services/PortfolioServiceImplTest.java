package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioName;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.DataImporter;
import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportTestServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        InProcessNeo4jServerModule.class,
        ImportTestServiceModule.class,
        RepositoryModule.class
})
public class PortfolioServiceImplTest {

    @Inject
    private DataImporter importService;

    @Inject
    private PortfolioService portfolioService;

    @Inject
    private AgreementService agreementService;

    private ClientId client999 = ClientId.fromString("999");

    @Before
    public void setup() {
        importService.reload();
    }

    @Test
    public void testSavePortfolio() {
        final Portfolio portfolio = createPortfolio("p1");
        Portfolio save = portfolioService.save(portfolio);
        assertThat(save).isNotNull();
    }

    @Test
    public void testLoadPortfolio() {
        Portfolio p2 = portfolioService.portfolio(client999, PortfolioName.fromString("p2a"));
        assertThat(p2).isNull();

        p2 = createPortfolio("p2");
        portfolioService.save(p2);
        p2 = portfolioService.portfolio(client999, PortfolioName.fromString("p2a"));
        assertThat(p2).isNotNull();
    }

    private Portfolio createPortfolio(String name) {
        final Agreement agreement = agreementService.agreementFor(client999, PortfolioName.fromString(name));
        Portfolio portfolio = new Portfolio();
        portfolio.setName(PortfolioName.fromString(name+"a"));
        portfolio.setAgreement(agreement);
        return portfolio;
    }
}