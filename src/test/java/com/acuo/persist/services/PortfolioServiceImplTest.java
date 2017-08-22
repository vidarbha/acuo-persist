package com.acuo.persist.services;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.Portfolio;
import com.acuo.common.model.ids.PortfolioId;
import com.acuo.persist.modules.*;
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
public class PortfolioServiceImplTest {

    @Inject
    ImportService importService;

    @Inject
    PortfolioService portfolioService;

    @Before
    public void setUp() {
        importService.reload();
    }

    @Test
    public void testSavePortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setName("p1");
        portfolio.setPortfolioId(PortfolioId.fromString("p1"));

        Portfolio save = portfolioService.save(portfolio);
        assertThat(save).isNotNull();
    }

    @Test
    public void testLoadPortfolio() {
        Portfolio p2 = portfolioService.find(PortfolioId.fromString("p2"));
        assertThat(p2).isNotNull();
    }
}