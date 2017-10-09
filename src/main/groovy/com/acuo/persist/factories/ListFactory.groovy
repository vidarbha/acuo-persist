package com.acuo.persist.factories


class ListFactory extends AbstractFactory implements BuilderFactory {

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        return new ArrayList()
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        parent << child
    }

    @Override
    String name() {
        return "list"
    }
}
