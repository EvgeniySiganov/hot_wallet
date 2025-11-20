package ru.siganov.hot_wallet.exception;

import java.util.UUID;

public class NonExistentWalletException extends RuntimeException {
    public NonExistentWalletException(UUID uuid) {
        super("Non existent wallet: " + uuid);
    }
}
