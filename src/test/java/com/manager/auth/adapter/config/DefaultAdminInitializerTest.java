package com.manager.auth.adapter.config;

import com.manager.auth.adapter.config.admin.AdminConfigurationProperties;
import com.manager.auth.adapter.config.admin.DefaultAdminInitializer;
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

        AdminConfigurationProperties adminConfigurationProperties = mock(AdminConfigurationProperties.class);
        when(adminConfigurationProperties.email()).thenReturn("admin@payments.local");
        when(adminConfigurationProperties.password()).thenReturn("Secret123!");

        DefaultAdminInitializer initializer = new DefaultAdminInitializer(userRepository, passwordEncoder,
                adminConfigurationProperties);

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

        AdminConfigurationProperties adminConfigurationProperties = mock(AdminConfigurationProperties.class);
        when(adminConfigurationProperties.email()).thenReturn("admin@payments.local");
        when(adminConfigurationProperties.password()).thenReturn("Secret123!");

        DefaultAdminInitializer initializer = new DefaultAdminInitializer(userRepository, passwordEncoder,
                adminConfigurationProperties);

        initializer.run(applicationArguments);

        verify(userRepository).existsByEmail("admin@payments.local");
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }
}
