package com.manager.email.application.service;

import com.manager.email.model.ExpiredReceiptEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EmailTemplateService {

    private static final String VERIFICATION_EMAIL_TEMPLATE_PATH = "templates/email/verification-email.html";
    private static final String EXPIRED_RECEIPT_PERIODIC_TEMPLATE_PATH = "templates/email/expired-receipt-periodic" +
            ".html";
    private static final String EXPIRED_RECEIPT_UNIQUE_TEMPLATE_PATH = "templates/email/expired-receipt-unique.html";

    private final Map<String, String> templateCache = new ConcurrentHashMap<>();

    private EmailTemplateService() {
    }

    public String loadVerificationEmailTemplate(String verificationCode, String supportEmail) {
        String template = loadTemplate(VERIFICATION_EMAIL_TEMPLATE_PATH);
        return template
                .replace("{verificationCode}", verificationCode)
                .replace("{supportEmail}", supportEmail);
    }

    public String loadExpiredReceiptEmailTemplate(ExpiredReceiptEmailRequest request, String supportEmail) {
        boolean isPeriodic = request.periodStartDate() != null && request.periodEndDate() != null;
        String path = isPeriodic ? EXPIRED_RECEIPT_PERIODIC_TEMPLATE_PATH : EXPIRED_RECEIPT_UNIQUE_TEMPLATE_PATH;
        String template = loadTemplate(path);

        template = template
                .replace("{playerName}", request.playerName())
                .replace("{playerPersonalId}", request.playerPersonalId())
                .replace("{paymentName}", request.paymentName())
                .replace("{amount}", request.amount().toString())
                .replace("{expiryDate}", request.expiryDate().toString())
                .replace("{supportEmail}", supportEmail);

        if (isPeriodic) {
            template = template
                    .replace("{periodStartDate}", request.periodStartDate().toString())
                    .replace("{periodEndDate}", request.periodEndDate().toString());
        }

        return template;
    }

    private String loadTemplate(String path) {
        return templateCache.computeIfAbsent(path, key -> {
            try {
                ClassPathResource resource = new ClassPathResource(key);
                return resource.getContentAsString(StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("Failed to load email template from {}", key, e);
                throw new RuntimeException("Failed to load email template: " + key, e);
            }
        });
    }
}
