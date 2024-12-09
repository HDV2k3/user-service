package com.user.identity.service;

import com.user.identity.repository.UserPaymentRepository;
import com.user.identity.repository.entity.User;
import com.user.identity.exception.AppException;
import com.user.identity.exception.ErrorCode;
import com.user.identity.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VerificationTokenService {

    UserRepository userRepository;
    EmailService emailService;
    UserPaymentRepository userPaymentRepository;

    /**
     * Creates and assigns a verification token to the given user.
     *
     * @param user  the user entity to assign the token to
     * @param token the token to be set for verification
     */
    public void createVerificationToken(User user, String token) {
        user.setVerificationToken(token);
        userRepository.save(user);
        log.info("Verification token created for user: {}", user.getEmail());
    }

    /**
     * Validates the verification token by checking if it exists and enables the user if valid.
     *
     * @param token the verification token to validate
     * @return validation status as "valid" or "invalid"
     */
    public String validateVerificationToken(String token) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);

        if (userOpt.isEmpty()) {
            log.warn("Invalid verification token provided.");
            return "invalid";
        }

        User user = userOpt.get();
        user.setEnabled(true);
        userRepository.save(user);
        log.info("User verified successfully with token: {}", token);
        return "valid";
    }

    /**
     * Verifies email using a verification token and returns response data with email and success message.
     *
     * @param token the verification token
     * @return a map containing email and success message
     */
    public Map<String, Object> verifyEmail(String token) {
        Map<String, Object> response = new HashMap<>();
        String validationResult = validateVerificationToken(token);


        if ("valid".equals(validationResult)) {
            User user = userRepository.findByVerificationToken(token)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            userPaymentRepository.createPayment(user.getId());
            response.put("email", user.getEmail());
            response.put("message", "Email verified successfully! You can now log in.");
        } else {
            response.put("message", "Invalid verification token.");
        }

        return response;
    }
    /**
     * Resends verification email to the user if they haven't been verified yet.
     *
     * @param email the verification token
     * @return a message indicating the result of the resend action
     * @throws MessagingException if there is an error while sending the email
     */
    public String resendVerification(String email) throws MessagingException {

//        User user = userRepository.findByVerificationToken(email)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NULL));

        if (user.isEnabled()) {
            log.info("User with email {} is already verified.", user.getEmail());
            return "User is already verified";
        }

        String confirmationUrl = generateConfirmationUrl(user.getVerificationToken());
        emailService.sendVerificationEmail(user.getEmail(), user.getFirstName(), user.getLastName(), confirmationUrl);

        log.info("Verification email resent to user: {}", user.getEmail());
        return "Verification email resent";
    }

    /**
     * Generates the URL for email verification based on the token.
     *

     * @return a full URL for verification
     */

    private String generateConfirmationUrl(String token) {

        return "http://localhost:3000/verify-email/"+token;
    }
}
