package ru.siganov.hot_wallet;

import ru.siganov.hot_wallet.dto.OperationType;
import ru.siganov.hot_wallet.dto.WalletOperationRequest;

import java.math.BigDecimal;
import java.util.UUID;

public class TestUtils {

    private static final UUID POSITIVE_WALLET_UUID = UUID.fromString("8cc1422d-1f50-4941-b118-34c57d6d2b6f");

    public static WalletOperationRequest createValidDepositRequest() {
        return new WalletOperationRequest(
                POSITIVE_WALLET_UUID,
                OperationType.DEPOSIT,
                BigDecimal.valueOf(100.1)
        );
    }
}
