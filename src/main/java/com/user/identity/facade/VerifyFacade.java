package com.user.identity.facade;

import com.user.identity.exception.AppException;

import com.user.identity.service.VerificationTokenService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerifyFacade {
    private final VerificationTokenService verificationTokenService;
    public Map<String, Object> verifyEmail(String token) throws AppException {
        try {
            return verificationTokenService.verifyEmail(token);
        } catch (AppException e) {
            log.error("Error verifying email: {}", e.getMessage());
            throw e;
        }
    }

    public String resendVerification(String email) throws AppException {
        try {
            return verificationTokenService.resendVerification(email);
        } catch (AppException e) {
            log.error("Error resending verification: {}", e.getMessage());
            throw e;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
