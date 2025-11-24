package com.iremayvaz.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // @RestController sınıfları için global exception yakalayıcı tanımlar.
public class GlobalExceptionHandler {
    // İki istek aynı anda addToCart çağırdı : ÇAKIŞMA
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLock(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body("Sepet başka bir işlem tarafından güncellendi. Lütfen tekrar deneyin.");
    }
}
