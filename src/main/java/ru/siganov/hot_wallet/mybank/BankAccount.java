package ru.siganov.hot_wallet.mybank;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    @Getter
    private BigDecimal balance;

    private final ReentrantLock lock = new ReentrantLock();

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public BankAccount(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean withdraw(int i) {
        balance = balance.subtract(BigDecimal.valueOf(i));
        return true;
    }

    public boolean deposit(int i) {
        balance = balance.add(BigDecimal.valueOf(i));
        return true;
    }
}
