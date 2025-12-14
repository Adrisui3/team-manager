package com.manager.auth.model.users;

import com.manager.auth.model.exceptions.*;
import com.manager.auth.model.roles.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private UUID id;

    private String email;
    private String password;

    private String name;
    private String surname;

    private LocalDateTime lastLogIn;

    private Role role;

    private boolean enabled;

    private UserVerification verification;

    public void authenticate(String password, PasswordEncoder passwordEncoder) {
        if (!isEnabled()) {
            throw new DisabledUserException();
        }

        if (!passwordEncoder.matches(password, getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }

        setLastLogIn(LocalDateTime.now());
    }

    public void setPassword(String verificationCode, String newPassword, PasswordEncoder passwordEncoder) {
        if (getVerification() == null) {
            throw new VerificationNotFound();
        }

        if (getVerification().getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new VerificationExpiredException();
        }

        if (getVerification().getVerificationCode().equals(verificationCode)) {
            if (newPassword.isBlank())
                throw new InvalidPasswordException("Password cannot be blank");

            setEnabled(true);
            setPassword(passwordEncoder.encode(newPassword));
            setVerification(null);
        } else {
            throw new InvalidVerificationCodeException();
        }
    }

    public void changePassword(String email, String oldPassword, String newPassword, PasswordEncoder passwordEncoder) {
        if (!email.equals(this.email) || !passwordEncoder.matches(oldPassword, getPassword())) {
            throw new InvalidPasswordChangeException();
        }

        if (passwordEncoder.matches(newPassword, getPassword())) {
            throw new EqualNewPasswordException();
        }

        if (newPassword.isBlank())
            throw new InvalidPasswordException("Password cannot be blank");

        setPassword(passwordEncoder.encode(newPassword));
    }

    public void setVerification() {
        UserVerification userVerification = UserVerificationFactory.build(this);
        setVerification(userVerification);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDateTime getLastLogIn() {
        return lastLogIn;
    }

    public void setLastLogIn(LocalDateTime lastLogIn) {
        this.lastLogIn = lastLogIn;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UserVerification getVerification() {
        return verification;
    }

    public void setVerification(UserVerification verification) {
        this.verification = verification;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
