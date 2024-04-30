package com.flab.fpay.service.bank;

import com.flab.fpay.exception.BankException;

public interface BankService {
    void chargePaymentMoney(long memberId, int amount) throws BankException;
}
