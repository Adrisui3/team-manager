package com.manager.payments.adapter.out.persistence.players;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "player")
public class PlayerJpaEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String personalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String email;

    private String secondaryEmail;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String phoneNumber;

    private String secondaryPhoneNumber;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerGender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerStatus status;

    @PrePersist
    public void prePersist() {
        setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(LocalDateTime.now());
    }
}
