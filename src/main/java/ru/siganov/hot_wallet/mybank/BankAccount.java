package ru.siganov.hot_wallet.mybank;

import java.math.BigDecimal;

public class BankAccount {

    private BigDecimal balance;

    public BankAccount(BigDecimal balance) {
        this.balance = balance;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public synchronized BigDecimal withdraw(int i) {
        balance = balance.subtract(BigDecimal.valueOf(i));
        return balance;
    }

    public synchronized boolean deposit(int i) {
        balance = balance.add(BigDecimal.valueOf(i));
        return true;
    }
}
