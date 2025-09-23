package com.manager.auth.model.users;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class UserVerificationFactory {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int NUM_BYTES = 16;
    private static final int USER_VERIFICATION_TIME_MINUTES = 15;

    private UserVerificationFactory() {
    }

    public static UserVerification build(User user) {
        UserVerification userVerification = new UserVerification();
        userVerification.setVerificationCode(generateToken());
        userVerification.setExpirationDate(LocalDateTime.now().plusMinutes(USER_VERIFICATION_TIME_MINUTES));
        userVerification.setUserId(user.getId());

        return userVerification;
    }

    private static String generateToken() {
        byte[] bytes = new byte[NUM_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
