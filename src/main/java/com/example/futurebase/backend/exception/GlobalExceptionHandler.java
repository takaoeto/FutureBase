package com.example.futurebase.backend.exception;

import com.example.futurebase.backend.dto.response.ApiResponse.ErrorDetail;
import com.example.futurebase.backend.dto.response.ApiResponse.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final String TRACE_HEADER = "X-Trace-Id";

        /**
         * バリデーションエラー
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                String traceId = generateTraceId();
                List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                                .map(e -> new ErrorDetail(e.getField(), e.getDefaultMessage()))
                                .toList();

                log.warn("[{}] Validation error: {}", traceId, details);

                ErrorResponse body = new ErrorResponse(
                                ErrorCode.VALIDATION_ERROR.name(),
                                "Invalid request",
                                details,
                                traceId);
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .header(TRACE_HEADER, traceId)
                                .body(body);
        }

        /**
         * アプリケーション固有例外
         */
        @ExceptionHandler(AppException.class)
        public ResponseEntity<ErrorResponse> handleAppException(
                        AppException ex,
                        HttpServletRequest request) {
                String traceId = generateTraceId();
                log.warn("[{}] AppException: {} - {}", traceId, ex.getErrorCode(), ex.getMessage());

                ErrorResponse body = new ErrorResponse(
                                ex.getErrorCode().name(),
                                ex.getMessage(),
                                List.of(),
                                traceId);
                return ResponseEntity
                                .status(ex.getHttpStatus())
                                .header(TRACE_HEADER, traceId)
                                .body(body);
        }

        /**
         * 予期しない例外
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleUnexpected(
                        Exception ex,
                        HttpServletRequest request) {
                String traceId = generateTraceId();
                log.error("[{}] Unexpected error", traceId, ex);

                ErrorResponse body = new ErrorResponse(
                                ErrorCode.INTERNAL_SERVER_ERROR.name(),
                                "Internal server error",
                                List.of(),
                                traceId);
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .header(TRACE_HEADER, traceId)
                                .body(body);
        }

        private String generateTraceId() {
                return UUID.randomUUID().toString();
        }
}
