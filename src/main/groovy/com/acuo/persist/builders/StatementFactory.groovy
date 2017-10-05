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

    boolean isLeaf() {
        return false
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        MarginStatement statement = null
        if (attributes != null)
            statement = new MarginStatement(attributes)
        else
            statement = new MarginStatement()
        return statement
    }

    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object statement) {
        marginStatementService.save(statement)
    }

    @Override
    String name() {
        return "statement"
    }
}
