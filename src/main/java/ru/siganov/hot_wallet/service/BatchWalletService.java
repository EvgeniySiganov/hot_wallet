package ru.siganov.hot_wallet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.siganov.hot_wallet.dto.WalletOperationHolder;
import ru.siganov.hot_wallet.dto.WalletOperationRequest;
import ru.siganov.hot_wallet.exception.InsufficientBalanceException;
import ru.siganov.hot_wallet.exception.WalletBatchUpdateException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableScheduling
public class BatchWalletService implements WalletService {

    private final WalletRepositoryCustom repository;

    private final BlockingQueue<WalletOperationHolder> depositQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<WalletOperationHolder> withdrawQueue = new LinkedBlockingQueue<>();

    public BatchWalletService(WalletRepositoryCustom repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<String> getBalance(UUID uuid) {
        return ResponseEntity.ok().body(repository.getBalance(uuid).toPlainString());
    }

    @Override
    public CompletableFuture<String> addDepositOperation(WalletOperationRequest request) {
        WalletOperationHolder holder = new WalletOperationHolder(request);
        depositQueue.add(holder);
        log.info("Add deposit operation: {}", holder.walletOperationRequest());
        return holder.completableFuture();
    }

    @Override
    public CompletableFuture<String> addWithdrawOperation(WalletOperationRequest request) {
        WalletOperationHolder holder = new WalletOperationHolder(request);
        withdrawQueue.add(holder);
        log.info("Add withdraw operation: {}", holder.walletOperationRequest());
        return holder.completableFuture();
    }


    @Scheduled(fixedDelayString = "${hot-wallet.batch.deposit-delay-ms:100}")
    public void processDepositBatch() {
        log.debug("Processing deposit batch");
        List<WalletOperationHolder> batch = new ArrayList<>();
        depositQueue.drainTo(batch);
        Map<UUID, BigDecimal> map = batch.stream()
                .collect(Collectors.toMap(
                        h -> h.walletOperationRequest().walletId(),
                        h -> h.walletOperationRequest().amount(),
                        BigDecimal::add

                ));
        if (map.isEmpty()) {
            return;
        }
        try {
            repository.deposit(map);
            log.info("Deposit batch processed: {}", batch);
            batch.forEach(holder -> holder.completableFuture().complete("OK"));
        } catch (Exception e) {
            batch.forEach(holder -> holder.completableFuture().completeExceptionally(
                    new WalletBatchUpdateException(e.getMessage())
            ));
        }

    }

    @Scheduled(fixedDelayString = "${hot-wallet.batch.withdrawal-delay-ms:1000}")
    public void processWithdrawBatch() {
        List<WalletOperationHolder> batch = new ArrayList<>();
        withdrawQueue.drainTo(batch);
        Map<WalletOperationHolder, BigDecimal> map = batch.stream()
                .collect(Collectors.toMap(
                        h -> h,
                        h -> h.walletOperationRequest().amount(),
                        BigDecimal::add

                ));
        if (map.isEmpty()) {
            return;
        }
        for (Map.Entry<WalletOperationHolder, BigDecimal> entry : map.entrySet()) {
            try {
                repository.withdraw(entry.getKey().walletOperationRequest().walletId(), entry.getValue());
                entry.getKey().completableFuture().complete("OK");
            } catch (InsufficientBalanceException e) {
                entry.getKey().completableFuture().completeExceptionally(e);
            }
        }
    }
}
