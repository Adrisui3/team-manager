package com.manager.auth.model.users;

import com.manager.auth.model.exceptions.*;
import com.manager.auth.model.roles.Role;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record User(UUID id, String email, String password, String name, String surname, LocalDateTime lastLogIn,
                   Role role, boolean enabled, UserVerification verification) {

    public User authenticate(String password, PasswordEncoder passwordEncoder) {
        if (!enabled()) {
            throw new DisabledUserException();
        }

        if (!passwordEncoder.matches(password, password())) {
            throw new InvalidEmailOrPasswordException();
        }

        return toBuilder().lastLogIn(LocalDateTime.now()).build();
    }

    public User setPassword(String verificationCode, String newPassword, PasswordEncoder passwordEncoder,
                            LocalDateTime currentTime) {
        if (verification() == null) {
            throw new VerificationNotFound();
        }

        if (verification().expirationDate().isBefore(currentTime)) {
            throw new VerificationExpiredException();
        }

        if (verification().verificationCode().equals(verificationCode)) {
            return toBuilder().enabled(true).password(passwordEncoder.encode(newPassword)).verification(null).build();
        } else {
            throw new InvalidVerificationCodeException();
        }
    }

    public User changePassword(String oldPassword, String newPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(oldPassword, password())) {
            throw new InvalidPasswordChangeException();
        }

        if (passwordEncoder.matches(newPassword, password())) {
            throw new EqualNewPasswordException();
        }

        return toBuilder().password(passwordEncoder.encode(newPassword)).build();
    }

    public User initializeVerification() {
        UserVerification userVerification = UserVerification.build(this);
        return toBuilder().verification(userVerification).build();
    }
}
