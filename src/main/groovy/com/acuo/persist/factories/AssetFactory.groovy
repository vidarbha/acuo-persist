package com.acuo.persist.factories

import com.acuo.common.model.ids.AssetId
import com.acuo.persist.entity.Agreement
import com.acuo.persist.entity.Asset
import com.acuo.persist.entity.Rule
import com.acuo.persist.services.AssetService

import javax.inject.Inject

class AssetFactory extends AbstractFactory implements BuilderFactory {

    private final AssetService service

    @Inject
    AssetFactory(AssetService service) {
        this.service = service
    }

    @Override
    String name() {
        return "asset"
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name, Object value, Map attributes
    ) throws InstantiationException, IllegalAccessException {
        return getOrCreate(attributes)
    }

    @Override
    void setParent(FactoryBuilderSupport builder,
                   Object parent, Object child) {
        if (parent != null) {
            switch (parent) {
                case Agreement:
                    parent.rules << child as Rule
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object child) {
        service.save(child)
    }

    private Asset getOrCreate(Map attributes) {
        Asset asset
        if (attributes != null) {
            String id = attributes["assetId"]
            if (id != null) {
                asset = service.find(AssetId.fromString(id))
            }
            if (asset == null) {
                return new Asset(attributes)
            } else {
                return asset
            }
        } else {
            asset = new Asset()
        }
        return asset
    }
}
