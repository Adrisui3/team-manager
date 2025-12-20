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
        if (userRepository.existsByEmail(adminConfigurationProperties.email())) {
            logger.info("Default admin user already present; skipping bootstrap");
            return;
        }

        User admin = User.builder()
                .email(adminConfigurationProperties.email())
                .password(passwordEncoder.encode(adminConfigurationProperties.password()))
                .name("Admin")
                .surname("Admin")
                .enabled(true)
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
        logger.info("Default admin user created");
    }
}
