package ru.siganov.hot_wallet.exception;

public class WalletBatchUpdateException extends RuntimeException {

    public WalletBatchUpdateException() {
        super("DB error while updating batch");
    }

    public WalletBatchUpdateException(String message) {
        super(message);
    }
}
