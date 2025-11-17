package ru.siganov.hot_wallet.dto;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

public record WalletOperationHolder(
        WalletOperationRequest walletOperationRequest,
        CompletableFuture<String> completableFuture
) {

    public WalletOperationHolder(WalletOperationRequest walletOperationRequest) {
        this(walletOperationRequest, new CompletableFuture<>());
    }
}
