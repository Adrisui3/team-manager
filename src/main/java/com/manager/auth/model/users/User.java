package com.manager.auth.model.users;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private UUID id;

    private String email;
    private String password;

    private String name;
    private String surname;

    private LocalDateTime lastLogIn;

    private boolean enabled;

    private UserVerification verification;

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
}
