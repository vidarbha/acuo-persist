package com.acuo.persist.services;

import com.acuo.common.model.ids.PortfolioId;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        RepositoryModule.class})
public class PortfolioServiceImplTest {

    @Inject
    PortfolioService portfolioService;

    @Test
    public void testSavePortfolio() {
        final Portfolio portfolio = createPortfolio("p1");
        Portfolio save = portfolioService.save(portfolio);
        assertThat(save).isNotNull();
    }

    @Test
    public void testLoadPortfolio() {
        Portfolio p2 = portfolioService.find(PortfolioId.fromString("p2"));
        assertThat(p2).isNull();

        p2 = createPortfolio("p2");
        portfolioService.save(p2);
        p2 = portfolioService.find(PortfolioId.fromString("p2"));
        assertThat(p2).isNotNull();
    }

    private Portfolio createPortfolio(String name) {
        Portfolio portfolio = new Portfolio();
        portfolio.setName(name);
        portfolio.setPortfolioId(PortfolioId.fromString(name));
        return portfolio;
    }
}