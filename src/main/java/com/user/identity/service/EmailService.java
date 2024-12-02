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
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      background-color: #f4f4f4;\n" +
                "    }\n" +
                "    .email-container {\n" +
                "      max-width: 600px;\n" +
                "      margin: 20px auto;\n" +
                "      background: #ffffff;\n" +
                "      border-radius: 8px;\n" +
                "      overflow: hidden;\n" +
                "      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "    .header {\n" +
                "      background-color: #4CAF50;\n" +
                "      color: #ffffff;\n" +
                "      text-align: center;\n" +
                "      padding: 20px;\n" +
                "      font-size: 24px;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "    .content {\n" +
                "      padding: 20px;\n" +
                "      text-align: left;\n" +
                "      color: #333333;\n" +
                "    }\n" +
                "    .content p {\n" +
                "      font-size: 16px;\n" +
                "      line-height: 1.6;\n" +
                "    }\n" +
                "    .cta-button {\n" +
                "      display: block;\n" +
                "      width: fit-content;\n" +
                "      margin: 20px auto;\n" +
                "      padding: 10px 20px;\n" +
                "      text-align: center;\n" +
                "      background-color: #4CAF50;\n" +
                "      color: #ffffff;\n" +
                "      text-decoration: none;\n" +
                "      font-size: 16px;\n" +
                "      font-weight: bold;\n" +
                "      border-radius: 4px;\n" +
                "      transition: background-color 0.3s ease;\n" +
                "    }\n" +
                "    .cta-button:hover {\n" +
                "      background-color: #45a049;\n" +
                "    }\n" +
                "    .footer {\n" +
                "      text-align: center;\n" +
                "      font-size: 12px;\n" +
                "      color: #666666;\n" +
                "      padding: 10px 20px;\n" +
                "      background-color: #f4f4f4;\n" +
                "      border-top: 1px solid #e0e0e0;\n" +
                "    }\n" +
                "    .footer a {\n" +
                "      color: #4CAF50;\n" +
                "      text-decoration: none;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"email-container\">\n" +
                "    <div class=\"header\">\n" +
                "      Verify Your Email\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "      <p>Hello " + firstName + " " + lastName + ",</p>\n" +
                "      <p>Thank you for signing up! Please click the button below to verify your email address and activate your account:</p>\n" +
                "      <a href=\"" + verificationUrl + "\" class=\"cta-button\">Verify Email</a>\n" +
                "      <p>If you didn't create an account with us, please ignore this email.</p>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "      <p>Need help? <a href=\"mailto:nextlife@odayok.com\">Contact Support</a></p>\n" +
                "      <p>&copy; 2024 NextLife. All rights reserved.</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>\n";
    }

}
