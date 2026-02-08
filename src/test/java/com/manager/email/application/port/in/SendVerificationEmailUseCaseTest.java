package com.manager.email.application.port.in;

import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.application.service.EmailTemplateService;
import com.manager.email.generator.EmailGenerator;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
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
public class SendVerificationEmailUseCaseTest {

    @MockitoBean
    private EmailRepository repository;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private SendVerificationEmailUseCase useCase;

    @Autowired
    private EmailTemplateService templateService;

    @Captor
    private ArgumentCaptor<Email> captor;

    @Test
    public void shouldSendSaveVerificationEmail() {
        String to = "test@test.com";
        String verificationCode = "12345";

        Email expected = EmailGenerator.email()
                .toEmail("test@test.com")
                .subject("Verification email")
                .body(templateService.loadVerificationEmailTemplate(verificationCode, "support@test.com"))
                .status(EmailStatus.PENDING)
                .build();

        when(emailService.getSupportEmail()).thenReturn("support@test.com");

        useCase.sendVerificationEmail(to, verificationCode);

        verify(repository).save(captor.capture());
        Email sentEmail = captor.getValue();
        assertThat(sentEmail).usingRecursiveComparison().ignoringFields("createdAt").isEqualTo(expected);
    }
}
