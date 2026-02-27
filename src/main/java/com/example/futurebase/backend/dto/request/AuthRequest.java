package com.example.futurebase.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// ---- Auth ----

public class AuthRequest {

        public record RegisterRequest(
                        @NotBlank @Email @Size(max = 255) String email,

                        @NotBlank @Size(min = 8, max = 100) String password) {
        }

        public record LoginRequest(
                        @NotBlank @Email @Size(max = 255) String email,

                        @NotBlank @Size(min = 1, max = 100) String password) {
        }

        public record OtpVerifyRequest(
                        @NotBlank @Email @Size(max = 255) String email,

                        @NotBlank @jakarta.validation.constraints.Pattern(regexp = "^[0-9]{6}$") String otp) {
        }
}
