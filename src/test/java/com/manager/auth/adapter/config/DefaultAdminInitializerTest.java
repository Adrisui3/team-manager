package com.manager.auth.adapter.config;

import com.manager.auth.application.port.out.UserRepository;
import com.manager.auth.model.roles.Role;
import com.manager.auth.model.users.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultAdminInitializerTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final ApplicationArguments applicationArguments = mock(ApplicationArguments.class);

    @Test
    void createsDefaultAdminWhenMissing() {
        when(userRepository.existsByEmail("admin@payments.local")).thenReturn(false);
        when(passwordEncoder.encode("Secret123!"))
                .thenReturn("encoded-secret");

        DefaultAdminInitializer initializer = new DefaultAdminInitializer(
                userRepository,
                passwordEncoder,
                "admin@payments.local",
                "Secret123!");

        initializer.run(applicationArguments);

        verify(userRepository).existsByEmail("admin@payments.local");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("admin@payments.local");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-secret");
        assertThat(savedUser.isEnabled()).isTrue();
        assertThat(savedUser.getRole()).isEqualTo(Role.ADMIN);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void skipsCreationWhenAdminAlreadyExists() {
        when(userRepository.existsByEmail("admin@payments.local")).thenReturn(true);

        DefaultAdminInitializer initializer = new DefaultAdminInitializer(
                userRepository,
                passwordEncoder,
                "admin@payments.local",
                "Secret123!");

        initializer.run(applicationArguments);

        verify(userRepository).existsByEmail("admin@payments.local");
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void skipsCreationWhenConfigurationMissing() {
        DefaultAdminInitializer initializerWithoutEmail = new DefaultAdminInitializer(
                userRepository,
                passwordEncoder,
                " ",
                "Secret123!");

        initializerWithoutEmail.run(applicationArguments);
        verifyNoInteractions(userRepository);

        DefaultAdminInitializer initializerWithoutPassword = new DefaultAdminInitializer(
                userRepository,
                passwordEncoder,
                "admin@payments.local",
                " ");

        initializerWithoutPassword.run(applicationArguments);
        verifyNoInteractions(userRepository);
    }
}
