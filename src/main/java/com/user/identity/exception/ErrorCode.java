package com.user.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    SEND_EMAIL_ERROR(1009, "Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ALREADY_EXISTED(1010, "User already existed", HttpStatus.BAD_REQUEST),

    // exception for user service
    CREATE_USER_ERROR(1011, "Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR),
    REQUEST_NULL(1012, "Request is null", HttpStatus.BAD_REQUEST),
    REQUEST_EMPTY(1013, "Request is empty", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1011, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_VERIFIED(1012, "User already verified", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_TOKEN(1013, "Invalid verification token", HttpStatus.BAD_REQUEST),
    USER_NOT_VERIFIED(1014, "User not verified", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_VERIFIED(1015, "Email not verified", HttpStatus.BAD_REQUEST),
    EMAIL_VERIFIED(1016, "Email verified", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_SENT(1017, "Email not sent", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_VERIFIED(1018, "Email already verified", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(1019, "Email not existed", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_MATCH(1020, "Email not match", HttpStatus.BAD_REQUEST),
    EMAIL_NULL(1021, "Email is null", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1022, "Email is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_NULL(1023, "Password is null", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY(1024, "Password is empty", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1025, "Password is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1026, "Password not match", HttpStatus.BAD_REQUEST),
    PASSWORD_SHORT(1027, "Password is too short", HttpStatus.BAD_REQUEST),
    FIRST_NAME_NULL(1028, "First name is null", HttpStatus.BAD_REQUEST),
    FIRST_NAME_EMPTY(1029, "First name is empty", HttpStatus.BAD_REQUEST),
    FIRST_NAME_INVALID(1030, "First name is invalid", HttpStatus.BAD_REQUEST),
    FIRST_NAME_NOT_MATCH(1031, "First name not match", HttpStatus.BAD_REQUEST),
    LAST_NAME_NULL(1030, "Last name is null", HttpStatus.BAD_REQUEST),
    LAST_NAME_EMPTY(1031, "Last name is empty", HttpStatus.BAD_REQUEST),
    LAST_NAME_INVALID(1032, "Last name is invalid", HttpStatus.BAD_REQUEST),
    LAST_NAME_NOT_MATCH(1033, "Last name not match", HttpStatus.BAD_REQUEST),
    DOB_NULL(1032, "Date of birth is null", HttpStatus.BAD_REQUEST),
    DOB_INVALID(1033, "Date of birth is invalid", HttpStatus.BAD_REQUEST),
    DOB_NOT_MATCH(1034, "Date of birth not match", HttpStatus.BAD_REQUEST),
    DOB_YOUNG(1035, "You are too young", HttpStatus.BAD_REQUEST),
    DOB_OLD(1036, "You are too old", HttpStatus.BAD_REQUEST),
    ROLE_NULL(1037, "Role is null", HttpStatus.BAD_REQUEST),
    ROLE_EMPTY(1038, "Role is empty", HttpStatus.BAD_REQUEST),
    SUBSCRIPTION_NOT_FOUND(1,"Not found",HttpStatus.NOT_FOUND),
    NOT_PREMIUM_USER(2,"Not macth",HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_AMOUNT(3,"Payment amount is invalid",HttpStatus.BAD_REQUEST),
    PAYMENT_FAILED(4,"Payment processing failed",HttpStatus.BAD_REQUEST),
    ALREADY_PREMIUM(5,"User is already a premium member",HttpStatus.BAD_REQUEST),
    SUBSCRIPTION_UPGRADE_FAILED(6,"Failed to upgrade subscription",HttpStatus.BAD_REQUEST);
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
