package com.acuo.persist.services;

import com.acuo.persist.entity.SettlementDate;

import java.time.LocalDate;

public interface SettlementDateService extends Service<SettlementDate, String> {

    SettlementDate createSettlementDate(LocalDate date);
}
