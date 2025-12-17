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

    public void transfer(BankAccount from, BankAccount to, int amount) {
        if (from == null || to == null || from == to) {
            throw new IllegalArgumentException("Wrong bank account");
        }

        int hashBankAccountFrom = System.identityHashCode(from);
        int hashBankAccountTo = System.identityHashCode(to);

        BankAccount first = hashBankAccountFrom < hashBankAccountTo ? from : to;
        BankAccount second = hashBankAccountFrom < hashBankAccountTo ? to : from;
        first.lock();
        second.lock();

        try {
            BigDecimal balAcc1 = from.getBalance().subtract(BigDecimal.valueOf(amount));
            if (balAcc1.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Not enough balance");
            } else {
                boolean withdraw = from.withdraw(amount);
                boolean deposit = to.deposit(amount);
                if (!deposit || !withdraw) {
                    throw new IllegalArgumentException("Transaction failed");
                }
            }
        } finally {
            first.unlock();
            second.unlock();
        }
    }

    public BigDecimal getTotalBalance() {
        return bankAccounts.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
