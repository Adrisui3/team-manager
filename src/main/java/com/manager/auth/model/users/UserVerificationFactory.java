package com.manager.auth.model.users;

import java.time.LocalDateTime;
import java.util.Random;

public class UserVerificationFactory {

    private static final int USER_VERIFICATION_TIME_MINUTES = 15;

    private UserVerificationFactory() {
    }

    public static UserVerification build(User user) {
        UserVerification userVerification = new UserVerification();
        userVerification.setVerificationCode(generateVerificationCode());
        userVerification.setExpirationDate(LocalDateTime.now().plusMinutes(USER_VERIFICATION_TIME_MINUTES));
        userVerification.setUserId(user.getId());

        return userVerification;
    }

    private static String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999) + 10000;
        return String.valueOf(code);
    }
}
