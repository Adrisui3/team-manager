package com.manager.auth.adapter.out.persistence.users;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_verification")
public class UserVerificationJpaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private UserJpaEntity user;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private String verificationCode;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
