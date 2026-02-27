package com.example.futurebase.backend.exception;

/**
 * アプリケーション固有の例外基底クラス
 */
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int httpStatus;

    public AppException(ErrorCode errorCode, int httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    // ---- 具体的な例外 ----

    public static class EmailAlreadyExistsException extends AppException {
        public EmailAlreadyExistsException() {
            super(ErrorCode.EMAIL_ALREADY_EXISTS, 409, "Email already registered");
        }
    }

    public static class InvalidCredentialsException extends AppException {
        public InvalidCredentialsException() {
            super(ErrorCode.UNAUTHORIZED, 401, "Invalid email or password");
        }
    }

    public static class AccountLockedException extends AppException {
        public AccountLockedException() {
            super(ErrorCode.ACCOUNT_LOCKED, 409, "Account locked");
        }
    }

    public static class AccountNotVerifiedException extends AppException {
        public AccountNotVerifiedException() {
            super(ErrorCode.UNAUTHORIZED, 401, "Account not verified");
        }
    }

    public static class OtpExpiredException extends AppException {
        public OtpExpiredException() {
            super(ErrorCode.OTP_EXPIRED, 409, "OTP expired");
        }
    }

    public static class OtpMismatchException extends AppException {
        public OtpMismatchException() {
            super(ErrorCode.OTP_MISMATCH, 409, "OTP mismatch");
        }
    }

    public static class OtpLockedException extends AppException {
        public OtpLockedException() {
            super(ErrorCode.OTP_LOCKED, 409, "Too many attempts. Account locked.");
        }
    }

    public static class OtpInvalidException extends AppException {
        public OtpInvalidException() {
            super(ErrorCode.OTP_INVALID, 401, "OTP invalid or expired");
        }
    }

    public static class ResourceNotFoundException extends AppException {
        public ResourceNotFoundException() {
            super(ErrorCode.NOT_FOUND, 404, "Resource not found");
        }
    }

    public static class ForbiddenException extends AppException {
        public ForbiddenException() {
            super(ErrorCode.FORBIDDEN, 403, "Access denied");
        }
    }
}
