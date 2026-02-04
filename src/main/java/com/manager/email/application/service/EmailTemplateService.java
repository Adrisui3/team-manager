package com.manager.email.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EmailTemplateService {

    private static final String VERIFICATION_EMAIL_TEMPLATE_PATH = "templates/email/verification-email.html";
    private static String verificationEmailTemplate;

    private EmailTemplateService() {
    }

    public String loadVerificationEmailTemplate(String verificationCode, String supportEmail) {
        String template = loadTemplate(VERIFICATION_EMAIL_TEMPLATE_PATH);
        return template
                .replace("{verificationCode}", verificationCode)
                .replace("{supportEmail}", supportEmail);
    }

    private String loadTemplate(String path) {
        if (verificationEmailTemplate == null) {
            try {
                ClassPathResource resource = new ClassPathResource(path);
                verificationEmailTemplate = resource.getContentAsString(StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("Failed to load email template from {}", path, e);
                throw new RuntimeException("Failed to load email template: " + path, e);
            }
        }
        return verificationEmailTemplate;
    }
}
