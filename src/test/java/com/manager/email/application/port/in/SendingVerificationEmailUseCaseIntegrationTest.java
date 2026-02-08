package com.manager.email.application.port.in;

import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.application.service.EmailTemplateService;
import com.manager.email.generator.EmailGenerator;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
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
class SendingVerificationEmailUseCaseIntegrationTest {

    @Autowired
    private EmailRepository repository;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private SendVerificationEmailUseCase useCase;

    @Autowired
    private EmailTemplateService templateService;

    @BeforeEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveVerificationEmail() {
        String to = "test@test.com";
        String verificationCode = "12345";

        when(emailService.getSupportEmail()).thenReturn("support@test.com");

        useCase.sendVerificationEmail(to, verificationCode);

        Email expected = EmailGenerator.email()
                .toEmail("test@test.com")
                .subject("Verification email")
                .body(templateService.loadVerificationEmailTemplate(verificationCode, "support@test.com"))
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
