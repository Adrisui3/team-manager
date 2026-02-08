package com.manager.email.application.port.in;

import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.generator.EmailGenerator;
import com.manager.email.model.Email;
import com.manager.email.model.EmailFailedException;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SendingPendingEmailUseCaseTest {

    @MockitoBean
    private EmailRepository repository;

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private SendPendingEmailsUseCase useCase;

    @Captor
    private ArgumentCaptor<Email> captor;

    @Test
    void sendsPendingEmails() {
        Email first = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.PENDING)
                .build();
        when(repository.findAllToBeSent()).thenReturn(List.of(first));

        useCase.sendPendingEmails();

        Email expectedSent = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.SENT)
                .build();

        verify(emailService).sendEmail(any(Email.class));
        verify(repository).save(captor.capture());
        Email updatedEmail = captor.getValue();
        assertThat(updatedEmail)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "sentAt")
                .isEqualTo(expectedSent);
        assertThat(updatedEmail.sentAt()).isNotNull();
        assertThat(updatedEmail.createdAt()).isNotNull();
    }

    @Test
    void updatesEmailStatusToErroredWhenFailing() {
        Email first = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.PENDING)
                .build();
        when(repository.findAllToBeSent()).thenReturn(List.of(first));
        doThrow(new EmailFailedException(""))
                .when(emailService)
                .sendEmail(any());

        useCase.sendPendingEmails();

        Email expectedSaved = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.ERRORED)
                .build();

        verify(repository).save(captor.capture());
        Email savedEmail = captor.getValue();
        assertThat(savedEmail).usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(expectedSaved);
        assertThat(savedEmail.sentAt()).isNull();
    }

    @Test
    void updatesEmailStatusToDiscardedWhenFailingTwice() {
        Email first = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.ERRORED)
                .build();
        when(repository.findAllToBeSent()).thenReturn(List.of(first));
        doThrow(new EmailFailedException(""))
                .when(emailService)
                .sendEmail(any());

        useCase.sendPendingEmails();

        Email expectedSaved = EmailGenerator.email()
                .toEmail("first@test.com")
                .subject("subject")
                .body("body")
                .status(EmailStatus.DISCARDED)
                .build();

        verify(repository).save(captor.capture());
        Email savedEmail = captor.getValue();
        assertThat(savedEmail).usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(expectedSaved);
        assertThat(savedEmail.sentAt()).isNull();
    }
}
