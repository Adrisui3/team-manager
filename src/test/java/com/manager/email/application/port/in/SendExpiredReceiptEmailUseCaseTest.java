package com.manager.email.application.port.in;

import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.application.service.EmailTemplateService;
import com.manager.email.generator.EmailGenerator;
import com.manager.email.generator.ExpiredReceiptEmailRequestGenerator;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
import com.manager.email.model.ExpiredReceiptEmailRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SendExpiredReceiptEmailUseCaseTest {

    @MockitoBean
    private EmailRepository repository;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private SendExpiredReceiptEmailUseCase useCase;

    @Autowired
    private EmailTemplateService templateService;

    @Captor
    private ArgumentCaptor<Email> captor;

    @Test
    public void shouldSaveExpiredReceiptEmailForPeriodicPayment() {
        ExpiredReceiptEmailRequest request = ExpiredReceiptEmailRequestGenerator.periodicRequest().build();

        when(emailService.getSupportEmail()).thenReturn("support@test.com");

        Email expected = EmailGenerator.email()
                .toEmail(request.playerEmail())
                .subject("Recibo vencido")
                .body(templateService.loadExpiredReceiptEmailTemplate(request, "support@test.com"))
                .status(EmailStatus.PENDING)
                .build();

        useCase.sendExpiredReceiptEmail(request);

        verify(repository).save(captor.capture());
        Email savedEmail = captor.getValue();
        assertThat(savedEmail).usingRecursiveComparison().ignoringFields("createdAt").isEqualTo(expected);
    }

    @Test
    public void shouldSaveExpiredReceiptEmailForUniquePayment() {
        ExpiredReceiptEmailRequest request = ExpiredReceiptEmailRequestGenerator.uniqueRequest().build();

        when(emailService.getSupportEmail()).thenReturn("support@test.com");

        Email expected = EmailGenerator.email()
                .toEmail(request.playerEmail())
                .subject("Recibo vencido")
                .body(templateService.loadExpiredReceiptEmailTemplate(request, "support@test.com"))
                .status(EmailStatus.PENDING)
                .build();

        useCase.sendExpiredReceiptEmail(request);

        verify(repository).save(captor.capture());
        Email savedEmail = captor.getValue();
        assertThat(savedEmail).usingRecursiveComparison().ignoringFields("createdAt").isEqualTo(expected);
    }
}
