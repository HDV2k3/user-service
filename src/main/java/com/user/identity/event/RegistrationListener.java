package com.user.identity.event;


import com.user.identity.repository.entity.User;
import com.user.identity.service.EmailService;
import com.user.identity.service.VerificationTokenService;
import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;



@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String confirmationUrl = "http://ec2-13-54-221-254.ap-southeast-2.compute.amazonaws.com:3000/verify-email";
        try {
            emailService.sendVerificationEmail(recipientAddress,user.getFirstName(),user.getLastName(), confirmationUrl);
        } catch (MessagingException e) {
            // Handle the exception (log it, notify the user, etc.)
            e.printStackTrace();
        }
    }
}
