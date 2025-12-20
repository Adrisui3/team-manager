package com.manager.auth.model.users;

import com.manager.auth.model.exceptions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class UserTest {

    @Test
    void shouldAuthenticateUserWhenPasswordIsValid() {
        //given
        User user = User.builder()
                .password("password")
                .enabled(true)
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("password"), anyString())).thenReturn(true);

        //when
        User authenticatedUser = user.authenticate("password", encoder);

        //then
        assertThat(authenticatedUser.lastLogIn()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNotValid() {
        //given
        User user = User.builder()
                .password("password")
                .enabled(true)
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("password"), anyString())).thenReturn(false);

        //when - then
        assertThatThrownBy(() -> user.authenticate("password", encoder)).isInstanceOf(InvalidEmailOrPasswordException.class);
    }

    @Test
    void shouldThrowExceptionWhenUserIsDisabled() {
        //given
        User user = User.builder()
                .password("password")
                .enabled(false)
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("password"), anyString())).thenReturn(true);

        //when - then
        assertThatThrownBy(() -> user.authenticate("password", encoder)).isInstanceOf(DisabledUserException.class);
    }

    @Test
    void shouldSetUserPasswordWhenVerificationCodeIsValid() {
        //given
        UserVerification userVerification = Mockito.mock(UserVerification.class);
        Mockito.when(userVerification.expirationDate()).thenReturn(LocalDateTime.of(2025, 12, 15, 10, 0));
        Mockito.when(userVerification.verificationCode()).thenReturn("1234");
        User user = User.builder()
                .verification(userVerification)
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.encode(eq("password"))).thenReturn("hashedPassword");

        LocalDateTime now = LocalDateTime.of(2025, 12, 14, 10, 0);

        //when
        User updatedUser = user.setPassword("1234", "password", encoder, now);

        //then
        assertThat(updatedUser.password()).isEqualTo("hashedPassword");
        assertThat(updatedUser.enabled()).isEqualTo(true);
        assertThat(updatedUser.verification()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenVerificationIsNull() {
        //given
        User user = User.builder().build();
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        LocalDateTime now = LocalDateTime.of(2025, 12, 14, 10, 0);

        //when
        assertThatThrownBy(() -> user.setPassword("1234", "password", encoder, now)).isInstanceOf(VerificationNotFound.class);
    }

    @Test
    void shouldThrowExceptionWhenVerificationIsExpired() {
        //given
        UserVerification userVerification = Mockito.mock(UserVerification.class);
        Mockito.when(userVerification.expirationDate()).thenReturn(LocalDateTime.of(2025, 12, 15, 10, 0));
        Mockito.when(userVerification.verificationCode()).thenReturn("1234");
        User user = User.builder().verification(userVerification).build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        LocalDateTime now = LocalDateTime.of(2025, 12, 20, 10, 0);

        //when
        assertThatThrownBy(() -> user.setPassword("1234", "password", encoder, now)).isInstanceOf(VerificationExpiredException.class);
    }

    @Test
    void shouldThrowExceptionWhenVerificationCodeIsNotCorrect() {
        //given
        UserVerification userVerification = Mockito.mock(UserVerification.class);
        Mockito.when(userVerification.expirationDate()).thenReturn(LocalDateTime.of(2025, 12, 15, 10, 0));
        Mockito.when(userVerification.verificationCode()).thenReturn("1234");
        User user = User.builder().verification(userVerification).build();
        ;

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.encode(eq("password"))).thenReturn("hashedPassword");

        LocalDateTime now = LocalDateTime.of(2025, 12, 14, 10, 0);

        //when
        assertThatThrownBy(() -> user.setPassword("4321", "password", encoder, now)).isInstanceOf(InvalidVerificationCodeException.class);
    }

    @Test
    void shouldChangePasswordWhenRequestIsValid() {
        //given
        User user = User.builder()
                .email("test@email.com")
                .password("oldPasswordHash")
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("oldPassword"), eq(user.password()))).thenReturn(true);
        Mockito.when(encoder.matches(eq("newPassword"), eq(user.password()))).thenReturn(false);
        Mockito.when(encoder.encode(eq("newPassword"))).thenReturn("newPasswordHash");

        //when
        User updatedUser = user.changePassword("test@email.com", "oldPassword", "newPassword", encoder);

        //then
        assertThat(updatedUser.password()).isEqualTo("newPasswordHash");
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotMatch() {
        //given
        User user = User.builder()
                .email("test@email.com")
                .password("oldPasswordHash")
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("oldPassword"), eq(user.password()))).thenReturn(true);
        Mockito.when(encoder.matches(eq("newPassword"), eq(user.password()))).thenReturn(false);
        Mockito.when(encoder.encode(eq("newPassword"))).thenReturn("newPasswordHash");

        //when - then
        assertThatThrownBy(() -> user.changePassword("test2@email.com", "oldPassword", "newPassword", encoder)).isInstanceOf(InvalidPasswordChangeException.class);
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordDoesNotMatch() {
        //given
        User user = User.builder()
                .email("test@email.com")
                .password("oldPasswordHash")
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("oldPassword"), eq(user.password()))).thenReturn(false);
        Mockito.when(encoder.matches(eq("newPassword"), eq(user.password()))).thenReturn(false);
        Mockito.when(encoder.encode(eq("newPassword"))).thenReturn("newPasswordHash");

        //when - then
        assertThatThrownBy(() -> user.changePassword("test@email.com", "oldPassword", "newPassword", encoder)).isInstanceOf(InvalidPasswordChangeException.class);
    }

    @Test
    void shouldThrowExceptionWhenCurrentAndNewPasswordMatch() {
        //given
        User user = User.builder()
                .email("test@email.com")
                .password("oldPasswordHash")
                .build();

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("oldPassword"), eq(user.password()))).thenReturn(true);
        Mockito.when(encoder.matches(eq("newPassword"), eq(user.password()))).thenReturn(true);
        Mockito.when(encoder.encode(eq("newPassword"))).thenReturn("newPasswordHash");

        //when - then
        assertThatThrownBy(() -> user.changePassword("test@email.com", "oldPassword", "oldPassword", encoder)).isInstanceOf(EqualNewPasswordException.class);
    }
}
