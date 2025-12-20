package com.manager.auth.adapter.config.admin;

import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultAdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminConfigurationProperties adminConfigurationProperties;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail(adminConfigurationProperties.email())) {
            log.info("Default admin user already present; skipping bootstrap");
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
        log.info("Default admin user created");
    }
}
