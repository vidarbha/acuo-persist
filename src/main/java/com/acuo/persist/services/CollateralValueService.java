package com.acuo.persist.services;

import com.acuo.persist.entity.CollateralValue;

public interface CollateralValueService extends Service<CollateralValue, Long> {

    CollateralValue createValue(Double amount);
}
