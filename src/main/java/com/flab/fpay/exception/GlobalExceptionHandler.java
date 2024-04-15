package com.flab.fpay.exception;

import com.flab.fpay.domain.common.ErrorCode;
import com.flab.fpay.domain.common.dto.response.FailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<FailResponse> handleApiException(ApiException exception) {
        this.logMessage(exception.getErrorCode(), exception);

        return ResponseEntity
                .status(exception.getErrorCode().getStatus())
                .body(new FailResponse(exception.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<FailResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new FailResponse("method not allowed"));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<FailResponse> handleMethodArgumentNotValidExceptionException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();

        for (FieldError f : fieldErrors) {
            errorMessage.append(f.getField())
                    .append(" ")
                    .append(f.getDefaultMessage());
        }

        FailResponse errorDto = new FailResponse(errorMessage.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    private void logMessage(ErrorCode errorCode, Exception exception) {
        HttpStatus httpStatus = errorCode.getStatus();

        if (httpStatus.is5xxServerError()) {
            log.error(errorCode.getMessage(), exception);
        }
    }
}
