package ru.siganov.hot_wallet.service;

import org.springframework.http.ResponseEntity;
import ru.siganov.hot_wallet.dto.WalletOperationRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public interface WalletService {
  CompletableFuture<String> addDepositOperation(WalletOperationRequest request);
  CompletableFuture<String> addWithdrawOperation(WalletOperationRequest request);

  ResponseEntity<String> getBalance(UUID uuid);
}
