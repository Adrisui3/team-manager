package com.manager.auth.application.service.email;

public interface EmailService {

    default void sendVerificationEmail(String to, String verificationCode) {
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">You have been invited to join our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please use the code below to set your password and activate your " +
                "account:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba" +
                "(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Invitation Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        sendEmail(to, "Verification email", htmlMessage);
    }

    void sendEmail(String to, String subject, String body);
}
