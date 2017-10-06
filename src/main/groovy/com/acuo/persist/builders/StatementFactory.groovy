package com.acuo.persist.builders

import com.acuo.persist.entity.MarginStatement
import com.acuo.persist.services.MarginStatementService

import javax.inject.Inject

class StatementFactory extends AbstractFactory implements BuilderFactory{

    private final MarginStatementService marginStatementService

    @Inject
    StatementFactory(MarginStatementService marginStatementService) {
        this.marginStatementService = marginStatementService
    }

    @Override
    String name() {
        return "statement"
    }

    boolean isLeaf() {
        return false
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        def statement
        if (attributes != null)
            statement = getOrCreate(attributes)
        else
            statement = new MarginStatement()
        return statement
    }

    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object statement) {
        marginStatementService.save(statement)
    }

    MarginStatement getOrCreate(Map attributes) {
        String id = attributes["statementId"]
        MarginStatement statement
        if(id != null) {
            statement = marginStatementService.find(id)
        }
        if(statement == null) {
            return new MarginStatement(attributes)
        } else
            return statement
    }
}
