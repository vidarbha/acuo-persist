package com.acuo.persist.builders


class StatementsFactory extends AbstractFactory implements BuilderFactory {

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        return new ArrayList()
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        parent << child
    }

    @Override
    String name() {
        return "statements"
    }
}
