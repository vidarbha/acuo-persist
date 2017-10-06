package com.acuo.persist.builders

import com.acuo.persist.entity.MarginStatement
import com.acuo.persist.services.MarginStatementService

import javax.inject.Inject

class StatementFactory extends AbstractFactory implements BuilderFactory{

    private final MarginStatementService service

    @Inject
    StatementFactory(MarginStatementService service) {
        this.service = service
    }

    @Override
    String name() {
        return "statement"
    }

    @Override
    boolean isLeaf() {
        return false
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        return getOrCreate(attributes)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent,
                         Object statement) {
        service.save(statement)
    }

    private MarginStatement getOrCreate(Map attributes) {
        MarginStatement statement
        if (attributes != null) {
            String id = attributes["statementId"]
            if (id != null) {
                statement = service.find(id)
            }
            if (statement == null) {
                return new MarginStatement(attributes)
            } else
                return statement
        } else {
            statement = new MarginStatement()
        }
        return statement
    }
}
