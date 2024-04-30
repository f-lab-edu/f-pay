package com.flab.fpay.service.bank.impl;

import com.flab.fpay.exception.BankException;
import com.flab.fpay.service.bank.BankService;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl implements BankService {

    @Override
    public void chargePaymentMoney(long memberId, int amount) throws BankException {
        // TODO: 2024/04/29 구현 필요
    }
}
