package com.example.futurebase.backend.dto.request;

import com.example.futurebase.backend.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DomainRequest {

        public record ProjectCreateRequest(
                        @NotBlank @Size(min = 1, max = 100) String name) {
        }

        public record ProjectUpdateRequest(
                        @NotBlank @Size(min = 1, max = 100) String name) {
        }

        public record TaskCreateRequest(
                        @NotBlank @Size(min = 1, max = 200) String title,

                        @NotNull TaskStatus status) {
        }

        public record TaskUpdateRequest(
                        @NotBlank @Size(min = 1, max = 200) String title,

                        @NotNull TaskStatus status) {
        }
}
