package com.payment.configuration;

import com.payment.model.ValidationError;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.stream.Collectors.toMap;
import static org.springframework.util.StringUtils.hasText;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationErrors(MethodArgumentNotValidException ex) {

        final var validationError = new ValidationError();
        validationError.setCount(ex.getBindingResult()
                .getErrorCount());
        validationError.setStatus(HttpStatus.BAD_REQUEST);
        validationError.setError("Validation failed.");

        final var pairs = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ValidationExceptionHandler::constructPair)
                .collect(toMap(Pair::getLeft, Pair::getRight));
        validationError.setErrors(pairs);
        return ResponseEntity.badRequest()
                .body(validationError);

    }

    private static ImmutablePair<String, Object> constructPair(ObjectError objectError) {
        return new ImmutablePair<>(objectError.getObjectName(),
                hasText(objectError.getDefaultMessage()) ? objectError.getDefaultMessage()
                        : objectError.getCode());
    }
}
