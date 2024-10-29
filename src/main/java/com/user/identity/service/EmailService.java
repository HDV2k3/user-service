package com.user.identity.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Sends a verification email to the specified recipient.
     *
     * @param to              the recipient's email address
     * @param firstName       the first name of the recipient
     * @param lastName        the last name of the recipient
     * @param verificationUrl the URL for email verification
     * @throws MessagingException if an error occurs while sending the email
     */
    public void sendVerificationEmail(String to, String firstName, String lastName, String verificationUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Email Verification");
        helper.setText(generateVerificationEmailContent(firstName, lastName, verificationUrl), true);

        mailSender.send(message);
    }

    /**
     * Generates the HTML content for the verification email.
     *
     * @param firstName       the first name of the recipient
     * @param lastName        the last name of the recipient
     * @param verificationUrl the URL for email verification
     * @return a String containing the HTML content for the email
     */
    private String generateVerificationEmailContent(String firstName, String lastName, String verificationUrl) {
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
                + ".container { width: 100%; padding: 20px; background-color: #f4f4f4; }"
                + ".email-content { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; background-color: #007bff; color: #ffffff; padding: 10px 0; border-radius: 5px 5px 0 0; }"
                + ".header h1 { margin: 0; font-size: 24px; }"
                + ".body { padding: 20px; text-align: center; }"
                + ".body p { font-size: 16px; color: #333333; line-height: 1.5; }"
                + ".body a { display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; }"
                + ".footer { text-align: center; margin-top: 20px; color: #777777; font-size: 12px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"email-content\">"
                + "<div class=\"header\">"
                + "<h1>Email Verification</h1>"
                + "</div>"
                + "<div class=\"body\">"
                + "<p>Hello " + firstName + lastName + ",</p>"
                + "<p>Thank you for registering with us. Please click the button below to verify your email address:</p>"
                + "<a href=\"" + verificationUrl + "\" target=\"_blank\">Verify Email</a>"
                + "<p>If you did not create an account, no further action is required.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>&copy; 2024 MyApp. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
