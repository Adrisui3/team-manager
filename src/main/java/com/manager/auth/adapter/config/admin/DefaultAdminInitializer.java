package com.manager.auth.adapter.config.admin;

import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminConfigurationProperties adminConfigurationProperties;

    public DefaultAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                   AdminConfigurationProperties adminConfigurationProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminConfigurationProperties = adminConfigurationProperties;
    }


    @Override
    public void run(ApplicationArguments args) {
        if (adminConfigurationProperties.email() == null || adminConfigurationProperties.email().isBlank()) {
            logger.warn("Default admin email is empty; skipping admin bootstrap");
            return;
        }

        if (adminConfigurationProperties.password() == null || adminConfigurationProperties.password().isBlank()) {
            logger.warn("Default admin password is empty; skipping admin bootstrap");
            return;
        }

        if (userRepository.existsByEmail(adminConfigurationProperties.email())) {
            logger.info("Default admin user already present; skipping bootstrap");
            return;
        }

        User admin = new User();
        admin.setEmail(adminConfigurationProperties.email());
        admin.setPassword(passwordEncoder.encode(adminConfigurationProperties.password()));
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setEnabled(true);
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);
        logger.info("Default admin user created");
    }
}
