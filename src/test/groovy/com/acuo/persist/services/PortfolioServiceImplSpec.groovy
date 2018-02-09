package com.acuo.persist.services

import com.acuo.common.ids.ClientId
import com.acuo.common.ids.PortfolioName
import com.acuo.common.ids.TradeId
import com.acuo.persist.entity.Portfolio
import org.neo4j.ogm.model.Result
import org.neo4j.ogm.session.Session
import spock.lang.Specification
import spock.lang.Subject

class PortfolioServiceImplSpec extends Specification {

    Session session = Mock()

    @Subject PortfolioService portfolioService = new PortfolioServiceImpl({->session})

    ClientId clientId = ClientId.fromString("999")

    def "find portfolio by trade id"() {
        given:
        def tradeId = TradeId.fromString("t1")

        when:
        portfolioService.findBy(tradeId)

        then:
        1 * session.queryForObject(Portfolio, _ as String, {it.any {key, value -> key == 'id' && value == 't1'} })
    }

    def "get all portfolios for a given list of portfolio of ids"() {
        given:
        def portfolioNames = ['p1', 'p2'].collect {p -> PortfolioName.fromString(p) }

        when:
        portfolioService.portfolios(clientId, *portfolioNames)

        then:
        1 * session.query(Portfolio, _ as String, { it['clientId'] == "999" && it['portfolioNames'] == [*portfolioNames]})
    }

    def "get a portfolio from a portfolio id"() {
        given:
        def portfolioName = PortfolioName.fromString('p1')
        def portfolio = Stub(Portfolio)

        when:
        portfolioService.portfolio(clientId, portfolioName)

        then:
        1 * session.query(Portfolio, _ as String, { it['clientId'] == "999" && it['portfolioNames'] == [portfolioName] }) >> [portfolio]
    }

    def "get the trade count"() {
        given:
        def portfolioName = PortfolioName.fromString('p1')
        def result = Stub(Result)
        result.iterator() >> [["count":1L]].iterator()

        when:
        def count = portfolioService.tradeCount(clientId, portfolioName)

        then:
        1 * session.query(_ as String, { it['clientId'] == "999" && it['portfolioName'] == portfolioName.toString() }) >> result
        count == 1L
    }
}
