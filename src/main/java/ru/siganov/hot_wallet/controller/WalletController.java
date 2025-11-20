package ru.siganov.hot_wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.siganov.hot_wallet.dto.WalletOperationRequest;
import ru.siganov.hot_wallet.service.WalletService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("wallet")
    public CompletableFuture<ResponseEntity<String>> updateWalletAmount(@RequestBody final WalletOperationRequest request) {
        return ("DEPOSIT".equals(request.operationType().name()) ?
                walletService.addDepositOperation(request) :
                walletService.addWithdrawOperation(request)).thenApply(ResponseEntity::ok);
    }

    @GetMapping("wallets/{uuid}")
    public ResponseEntity<String> getWallet(@PathVariable UUID uuid) {
        return walletService.getBalance(uuid);
    }
}
