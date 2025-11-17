package ru.siganov.hot_wallet.service;

import ru.siganov.hot_wallet.dto.WalletOperationRequest;

import java.util.concurrent.CompletableFuture;


public interface WalletService {
  CompletableFuture<String> addDepositOperation(WalletOperationRequest request);
  CompletableFuture<String> addWithdrawOperation(WalletOperationRequest request);
}
