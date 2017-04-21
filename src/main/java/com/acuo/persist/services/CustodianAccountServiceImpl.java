package com.acuo.persist.services;

import com.acuo.persist.entity.CustodianAccount;

public class CustodianAccountServiceImpl extends GenericService<CustodianAccount, String> implements CustodianAccountService {

    @Override
    public Class<CustodianAccount> getEntityType() {
        return CustodianAccount.class;
    }
}
