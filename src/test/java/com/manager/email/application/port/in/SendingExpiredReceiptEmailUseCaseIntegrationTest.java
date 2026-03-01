package com.manager.email.application.port.in;

import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.application.service.EmailTemplateService;
import com.manager.email.generator.EmailGenerator;
import com.manager.email.generator.ExpiredReceiptEmailRequestGenerator;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
import com.manager.email.model.ExpiredReceiptEmailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class SendingExpiredReceiptEmailUseCaseIntegrationTest {

    @Autowired
    private EmailRepository repository;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private SendExpiredReceiptEmailUseCase useCase;

    @Autowired
    private EmailTemplateService templateService;

    @BeforeEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveExpiredReceiptEmailForPeriodicPayment() {
        ExpiredReceiptEmailRequest request = ExpiredReceiptEmailRequestGenerator.periodicRequest().build();

        when(emailService.getSupportEmail()).thenReturn("support@test.com");

        useCase.sendExpiredReceiptEmail(request);

        Email expected = EmailGenerator.email()
                .toEmail(request.playerEmail())
                .subject("Recibo vencido")
                .body(templateService.loadExpiredReceiptEmailTemplate(request, "support@test.com"))
                .status(EmailStatus.PENDING)
                .build();

        List<Email> emails = repository.findAll();
        assertThat(emails).hasSize(1);
        Email savedEmail = emails.getFirst();
        assertThat(savedEmail)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(expected);
    }

    @Test
    void shouldSaveExpiredReceiptEmailForUniquePayment() {
        ExpiredReceiptEmailRequest request = ExpiredReceiptEmailRequestGenerator.uniqueRequest().build();

        when(emailService.getSupportEmail()).thenReturn("support@test.com");

        useCase.sendExpiredReceiptEmail(request);

        Email expected = EmailGenerator.email()
                .toEmail(request.playerEmail())
                .subject("Recibo vencido")
                .body(templateService.loadExpiredReceiptEmailTemplate(request, "support@test.com"))
                .status(EmailStatus.PENDING)
                .build();

        List<Email> emails = repository.findAll();
        assertThat(emails).hasSize(1);
        Email savedEmail = emails.getFirst();
        assertThat(savedEmail)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(expected);
    }
}
