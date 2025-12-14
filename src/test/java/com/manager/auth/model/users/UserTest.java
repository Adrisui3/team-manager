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
        User user = new User();
        user.setPassword("password");
        user.setEnabled(true);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("password"), anyString())).thenReturn(true);

        //when
        user.authenticate("password", encoder);

        //then
        assertThat(user.getLastLogIn()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNotValid() {
        //given
        User user = new User();
        user.setPassword("password");
        user.setEnabled(true);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("password"), anyString())).thenReturn(false);

        //when - then
        assertThatThrownBy(() -> user.authenticate("password", encoder)).isInstanceOf(InvalidEmailOrPasswordException.class);
    }

    @Test
    void shouldThrowExceptionWhenUserIsDisabled() {
        //given
        User user = new User();
        user.setPassword("password");
        user.setEnabled(false);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("password"), anyString())).thenReturn(true);

        //when - then
        assertThatThrownBy(() -> user.authenticate("password", encoder)).isInstanceOf(DisabledUserException.class);
    }

    @Test
    void shouldSetUserPasswordWhenVerificationCodeIsValid() {
        //given
        User user = new User();
        UserVerification userVerification = Mockito.mock(UserVerification.class);
        Mockito.when(userVerification.getExpirationDate()).thenReturn(LocalDateTime.of(2025, 12, 15, 10, 0));
        Mockito.when(userVerification.getVerificationCode()).thenReturn("1234");
        user.setVerification(userVerification);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.encode(eq("password"))).thenReturn("hashedPassword");

        LocalDateTime now = LocalDateTime.of(2025, 12, 14, 10, 0);

        //when
        user.setPassword("1234", "password", encoder, now);

        //then
        assertThat(user.getPassword()).isEqualTo("hashedPassword");
        assertThat(user.isEnabled()).isEqualTo(true);
        assertThat(user.getVerification()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenVerificationIsNull() {
        //given
        User user = new User();
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        LocalDateTime now = LocalDateTime.of(2025, 12, 14, 10, 0);

        //when
        assertThatThrownBy(() -> user.setPassword("1234", "password", encoder, now)).isInstanceOf(VerificationNotFound.class);
    }

    @Test
    void shouldThrowExceptionWhenVerificationIsExpired() {
        //given
        User user = new User();
        UserVerification userVerification = Mockito.mock(UserVerification.class);
        Mockito.when(userVerification.getExpirationDate()).thenReturn(LocalDateTime.of(2025, 12, 15, 10, 0));
        Mockito.when(userVerification.getVerificationCode()).thenReturn("1234");
        user.setVerification(userVerification);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        LocalDateTime now = LocalDateTime.of(2025, 12, 20, 10, 0);

        //when
        assertThatThrownBy(() -> user.setPassword("1234", "password", encoder, now)).isInstanceOf(VerificationExpiredException.class);
    }

    @Test
    void shouldThrowExceptionWhenVerificationCodeIsNotCorrect() {
        //given
        User user = new User();
        UserVerification userVerification = Mockito.mock(UserVerification.class);
        Mockito.when(userVerification.getExpirationDate()).thenReturn(LocalDateTime.of(2025, 12, 15, 10, 0));
        Mockito.when(userVerification.getVerificationCode()).thenReturn("1234");
        user.setVerification(userVerification);

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.encode(eq("password"))).thenReturn("hashedPassword");

        LocalDateTime now = LocalDateTime.of(2025, 12, 14, 10, 0);

        //when
        assertThatThrownBy(() -> user.setPassword("4321", "password", encoder, now)).isInstanceOf(InvalidVerificationCodeException.class);
    }

    @Test
    void shouldChangePasswordWhenRequestIsValid() {
        //given
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("oldPasswordHash");

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("oldPassword"), eq(user.getPassword()))).thenReturn(true);
        Mockito.when(encoder.matches(eq("newPassword"), eq(user.getPassword()))).thenReturn(false);
        Mockito.when(encoder.encode(eq("newPassword"))).thenReturn("newPasswordHash");

        //when
        user.changePassword("test@email.com", "oldPassword", "newPassword", encoder);

        //then
        assertThat(user.getPassword()).isEqualTo("newPasswordHash");
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotMatch() {
        //given
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("oldPasswordHash");

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("oldPassword"), eq(user.getPassword()))).thenReturn(true);
        Mockito.when(encoder.matches(eq("newPassword"), eq(user.getPassword()))).thenReturn(false);
        Mockito.when(encoder.encode(eq("newPassword"))).thenReturn("newPasswordHash");

        //when - then
        assertThatThrownBy(() -> user.changePassword("test2@email.com", "oldPassword", "newPassword", encoder)).isInstanceOf(InvalidPasswordChangeException.class);
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordDoesNotMatch() {
        //given
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("oldPasswordHash");

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("oldPassword"), eq(user.getPassword()))).thenReturn(false);
        Mockito.when(encoder.matches(eq("newPassword"), eq(user.getPassword()))).thenReturn(false);
        Mockito.when(encoder.encode(eq("newPassword"))).thenReturn("newPasswordHash");

        //when - then
        assertThatThrownBy(() -> user.changePassword("test@email.com", "oldPassword", "newPassword", encoder)).isInstanceOf(InvalidPasswordChangeException.class);
    }

    @Test
    void shouldThrowExceptionWhenCurrentAndNewPasswordMatch() {
        //given
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("oldPasswordHash");

        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(encoder.matches(eq("oldPassword"), eq(user.getPassword()))).thenReturn(true);
        Mockito.when(encoder.matches(eq("newPassword"), eq(user.getPassword()))).thenReturn(true);
        Mockito.when(encoder.encode(eq("newPassword"))).thenReturn("newPasswordHash");

        //when - then
        assertThatThrownBy(() -> user.changePassword("test@email.com", "oldPassword", "oldPassword", encoder)).isInstanceOf(EqualNewPasswordException.class);
    }
}
