package ru.siganov.hot_wallet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.siganov.hot_wallet.dto.ErrorResponse;

import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class WalletExceptionControllerAdvice {

    private static final Map<Class<? extends RuntimeException>, ErrorCode> ERROR_MAP = Map.of(
            WalletBatchUpdateException.class, ErrorCode.DB_ERROR,
            InsufficientBalanceException.class, ErrorCode.INSUFFICIENT_BALANCE,
            NonExistentWalletException.class, ErrorCode.NONE_EXIST_WALLET
    );

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception) {
        log.error(exception.getMessage(), exception);
        ErrorCode errorCode = Optional.ofNullable(ERROR_MAP.get(exception.getClass()))
                .orElse(ErrorCode.UNKNOWN_SERVER_ERROR);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(
                new ErrorResponse(errorCode, exception.getMessage()));
    }
}
