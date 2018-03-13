package com.acuo.persist.services

import com.acuo.common.model.ids.PortfolioId
import com.acuo.common.model.ids.TradeId
import com.acuo.persist.entity.Portfolio
import org.neo4j.ogm.model.Result
import org.neo4j.ogm.session.Session
import spock.lang.Specification
import spock.lang.Subject

class PortfolioServiceImplSpec extends Specification {

    Session session = Mock()

    @Subject PortfolioService portfolioService = new PortfolioServiceImpl({->session})


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
        def ids = ['p1', 'p2'].collect {p -> PortfolioId.fromString(p) }

        when:
        portfolioService.portfolios(*ids)

        then:
        1 * session.query(Portfolio, _ as String, {it.any {key, value -> key == 'ids' && value == [*ids]} })
    }

    def "get a portfolio from a portfolio id"() {
        given:
        def id = PortfolioId.fromString('p1')
        def portfolio = Stub(Portfolio)

        when:
        portfolioService.portfolio(id)

        then:
        1 * session.query(Portfolio, _ as String, {it.any {key, value -> key == 'ids' && value == [id]} }) >> [portfolio]
    }

    def "get the trade count"() {
        given:
        def id = PortfolioId.fromString('p1')
        def result = Stub(Result)
        result.iterator() >> [["count":1L]].iterator()

        when:
        def count = portfolioService.tradeCount(id)

        then:
        1 * session.query(_ as String, {it.any {key, value -> key == 'id' && value == 'p1'} }) >> result
        count == 1L
    }
}
