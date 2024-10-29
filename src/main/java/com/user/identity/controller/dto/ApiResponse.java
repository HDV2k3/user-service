package com.user.identity.controller.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    @Schema(description = "Response code indicating the result of the API call", example = "1019000")
    @Builder.Default
    Integer responseCode = 101000;

    @Schema(description = "Data or payload returned by the API")
    T data;

    String message;

    @JsonIgnore
    public boolean isSuccess() {
        return Objects.nonNull(responseCode) && isWithinSuccessRange(responseCode);
    }

    private boolean isWithinSuccessRange(int code) {
        int mainCode = code % 1000;
        return mainCode >= 900;
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .responseCode(200)
                .data(data)
                .message("Successfully")
                .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return ApiResponse.<T>builder()
                .responseCode(101001)
                .data(null)
                .message(errorMessage)
                .build();
    }
}
