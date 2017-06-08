package com.acuo.persist.services;

import com.acuo.persist.entity.CustodianAccount;
import com.acuo.persist.entity.LegalEntity;

public interface CustodianAccountService extends Service<CustodianAccount, String> {

    Iterable<CustodianAccount> custodianAccountsFor(LegalEntity legalEntity);

    Iterable<CustodianAccount> counterPartyCustodianAccountsFor(LegalEntity legalEntity);
}
