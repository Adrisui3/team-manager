package com.manager.email.application.port.out;

import com.manager.email.generator.EmailGenerator;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class EmailRepositoryTest {

    @Autowired
    private EmailRepository repository;

    @BeforeEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveEmail() {
        Email email = EmailGenerator.email().build();
        Email expected = EmailGenerator.email().build();

        repository.save(email);

        List<Email> emails = repository.findAll();
        assertThat(emails).hasSize(1);
        assertThat(emails.getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(expected);
    }

    @Test
    void shouldSaveMultipleEmailsToTheSameRecipient() {
        Email firstEmail = EmailGenerator.email()
                .toEmail("test@test.com")
                .build();
        Email secondEmail = EmailGenerator.email()
                .toEmail("test@test.com")
                .build();
        repository.save(firstEmail);
        repository.save(secondEmail);

        List<Email> emails = repository.findAll();
        assertThat(emails.size()).isEqualTo(2);
        assertThat(emails.stream().allMatch(email -> email.toEmail().equals("test@test.com"))).isTrue();
    }

    @Test
    void shouldFindEmailsToBeSent() {
        Email firstEmail = EmailGenerator.email()
                .toEmail("first@test.com")
                .status(EmailStatus.SENT)
                .build();
        Email secondEmail = EmailGenerator.email()
                .toEmail("second@test.com")
                .status(EmailStatus.DISCARDED)
                .build();
        Email thirdEmail = EmailGenerator.email()
                .toEmail("third@test.com")
                .status(EmailStatus.PENDING)
                .build();
        Email fourthEmail = EmailGenerator.email()
                .toEmail("fourth@test.com")
                .status(EmailStatus.ERRORED)
                .build();
        LocalDateTime targetTime = LocalDateTime.now().minusDays(1);

        repository.save(firstEmail);
        repository.save(secondEmail);
        repository.save(thirdEmail);
        repository.save(fourthEmail);

        List<Email> emails = repository.findAllToBeSent();
        assertThat(emails).hasSize(2).extracting(Email::toEmail)
                .containsExactlyInAnyOrder("third@test.com", "fourth@test.com");
    }

    @Test
    @Transactional
    void shouldDeleteExpiredEmails() {
        Email firstEmail = EmailGenerator.email()
                .toEmail("first@test.com")
                .status(EmailStatus.SENT)
                .build();
        Email secondEmail = EmailGenerator.email()
                .toEmail("second@test.com")
                .status(EmailStatus.DISCARDED)
                .build();
        Email thirdEmail = EmailGenerator.email()
                .toEmail("third@test.com")
                .status(EmailStatus.PENDING)
                .build();
        Email fourthEMail = EmailGenerator.email()
                .toEmail("fourth@test.com")
                .status(EmailStatus.ERRORED)
                .build();
        LocalDateTime targetTime = LocalDateTime.now().plusDays(1);

        repository.save(firstEmail);
        repository.save(secondEmail);
        repository.save(thirdEmail);
        repository.save(fourthEMail);

        int deleted = repository.deleteExpired(targetTime);
        assertThat(deleted).isEqualTo(2);
        List<Email> emails = repository.findAll();
        assertThat(emails).hasSize(2).extracting(Email::toEmail)
                .containsExactlyInAnyOrder("third@test.com", "fourth@test.com");
    }

    @Test
    void shouldFailWhenToEmailIsNull() {
        Email email = EmailGenerator.email().toEmail(null).build();
        assertThatThrownBy(() -> repository.save(email))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldFailWhenSubjectIsNull() {
        Email email = EmailGenerator.email().subject(null).build();
        assertThatThrownBy(() -> repository.save(email))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldFailWhenBodyIsNull() {
        Email email = EmailGenerator.email().body(null).build();
        assertThatThrownBy(() -> repository.save(email))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldFailWhenStatusIsNull() {
        Email email = EmailGenerator.email().status(null).build();
        assertThatThrownBy(() -> repository.save(email))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
