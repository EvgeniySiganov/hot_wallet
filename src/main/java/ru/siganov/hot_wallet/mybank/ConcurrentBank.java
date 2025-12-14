package ru.siganov.hot_wallet.mybank;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConcurrentBank {

    private final List<BankAccount> bankAccounts = new ArrayList<>();

    public BankAccount createAccount(int amount) {
        BankAccount account = new BankAccount(BigDecimal.valueOf(amount));
        bankAccounts.add(account);
        return account;

    }

    @Transactional
    public void transfer(BankAccount from, BankAccount to, int amount) {
        if (from == null || to == null || from == to) {
            throw new IllegalArgumentException("Wrong bank account");
        }
        BigDecimal balAcc1 = from.getBalance().subtract(BigDecimal.valueOf(amount));
        if (balAcc1.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Not enough balance");
        } else {
            BigDecimal withdraw = from.withdraw(amount);
            if (withdraw.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Negative balance");
            } else {
                boolean deposit = to.deposit(amount);
                if (!deposit) {
                    throw new IllegalArgumentException("Deposit failed");
                }
            }
        }
    }

    public BigDecimal getTotalBalance() {
        return bankAccounts.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
