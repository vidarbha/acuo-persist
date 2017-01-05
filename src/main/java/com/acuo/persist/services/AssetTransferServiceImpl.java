package com.acuo.persist.services;

import com.acuo.persist.entity.AssetTransfer;

public class AssetTransferServiceImpl extends GenericService<AssetTransfer> implements AssetTransferService{

    @Override
    public Class<AssetTransfer> getEntityType() {
        return AssetTransfer.class;
    }
}