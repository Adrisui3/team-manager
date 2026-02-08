package com.manager.email.application.port.in;

import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.generator.EmailGenerator;
import com.manager.email.model.Email;
import com.manager.email.model.EmailFailedException;
import com.manager.email.model.EmailStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class SendingPendingEmailUseCaseIntegrationTest {

    @Autowired
    private EmailRepository repository;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private SendPendingEmailsUseCase useCase;

    @BeforeEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    void sendsPendingEmails() {
        Email pending = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.PENDING)
                .build();
        repository.save(pending);

        useCase.sendPendingEmails(LocalDateTime.now().minusDays(1));

        Email expectedSent = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.SENT)
                .build();

        verify(emailService).sendEmail(any(Email.class));

        List<Email> emails = repository.findAll();
        assertThat(emails).hasSize(1);
        Email savedEmail = emails.getFirst();
        assertThat(savedEmail)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "sentAt")
                .isEqualTo(expectedSent);
        assertThat(savedEmail.sentAt()).isNotNull();
        assertThat(savedEmail.createdAt()).isNotNull();
    }

    @Test
    void updatesEmailStatusToErroredWhenFailing() {
        Email pending = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.PENDING)
                .build();
        repository.save(pending);

        doThrow(new EmailFailedException(""))
                .when(emailService)
                .sendEmail(any());

        useCase.sendPendingEmails(LocalDateTime.now().minusDays(1));

        Email expectedSaved = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.ERRORED)
                .build();

        List<Email> emails = repository.findAll();
        assertThat(emails).hasSize(1);
        Email savedEmail = emails.getFirst();
        assertThat(savedEmail)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(expectedSaved);
        assertThat(savedEmail.sentAt()).isNull();
    }

    @Test
    void updatesEmailStatusToDiscardedWhenFailingTwice() {
        Email errored = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.ERRORED)
                .build();
        repository.save(errored);

        doThrow(new EmailFailedException(""))
                .when(emailService)
                .sendEmail(any());

        useCase.sendPendingEmails(LocalDateTime.now().minusDays(1));

        Email expectedSaved = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.DISCARDED)
                .build();

        List<Email> emails = repository.findAll();
        assertThat(emails).hasSize(1);
        Email savedEmail = emails.getFirst();
        assertThat(savedEmail)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(expectedSaved);
        assertThat(savedEmail.sentAt()).isNull();
    }
}
