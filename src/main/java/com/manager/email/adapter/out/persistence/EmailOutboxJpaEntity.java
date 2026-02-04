package com.manager.email.adapter.out.persistence;

import com.manager.email.model.EmailStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "email_outbox")
public class EmailOutboxJpaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Email
    @Column(nullable = false)
    private String toEmail;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    @PrePersist
    public void prePersist() {
        setCreatedAt(LocalDateTime.now());
    }
}
