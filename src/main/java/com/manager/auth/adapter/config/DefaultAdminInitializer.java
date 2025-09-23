package com.manager.auth.adapter.config;

import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultAdminEmail;
    private final String defaultAdminPassword;

    public DefaultAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                   @Value("${admin.email}") String defaultAdminEmail,
                                   @Value("${admin.password}") String defaultAdminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultAdminEmail = defaultAdminEmail;
        this.defaultAdminPassword = defaultAdminPassword;
    }


    @Override
    public void run(ApplicationArguments args) {
        if (defaultAdminEmail == null || defaultAdminEmail.isBlank()) {
            logger.warn("Default admin email is empty; skipping admin bootstrap");
            return;
        }

        if (defaultAdminPassword == null || defaultAdminPassword.isBlank()) {
            logger.warn("Default admin password is empty; skipping admin bootstrap");
            return;
        }

        if (userRepository.existsByEmail(defaultAdminEmail)) {
            logger.info("Default admin user already present; skipping bootstrap");
            return;
        }

        User admin = new User();
        admin.setEmail(defaultAdminEmail);
        admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setEnabled(true);
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);
        logger.info("Default admin user created");
    }
}
