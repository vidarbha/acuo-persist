package com.acuo.persist.services;

import com.acuo.persist.entity.IRS;

public interface IRSService extends Service<IRS> {

    IRS findById(String id);
}
