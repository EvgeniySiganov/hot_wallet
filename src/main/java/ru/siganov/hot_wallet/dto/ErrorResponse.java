package ru.siganov.hot_wallet.dto;

import ru.siganov.hot_wallet.exception.ErrorCode;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {
}
