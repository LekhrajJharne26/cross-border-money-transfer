package com.crossborder.moneytransfer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@Getter @Builder @AllArgsConstructor @JsonInclude(JsonInclude.Include.NON_NULL)
/** Standard API response envelope used by controllers and centralized error handlers. */
public class ApiResponse<T> {
    private final Instant timestamp;
    private final boolean success;
    private final String message;
    private final T data;
    public static <T> ApiResponse<T> success(String message, T data) { return ApiResponse.<T>builder().timestamp(Instant.now()).success(true).message(message).data(data).build(); }
    public static ApiResponse<Void> failure(String message) { return ApiResponse.<Void>builder().timestamp(Instant.now()).success(false).message(message).build(); }
    public static <T> ApiResponse<T> failure(String message, T data) { return ApiResponse.<T>builder().timestamp(Instant.now()).success(false).message(message).data(data).build(); }
}
