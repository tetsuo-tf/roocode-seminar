package com.example.todoapp.service;

import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * UserServiceのテストクラス
 *
 * @author TodoApp Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setName("テストユーザー");
        testUser.setEnabled(true);
    }

    @Test
    void registerUser_成功() {
        // Given
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.registerUser(testUser);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        assertTrue(result.isEnabled());
        verify(userRepository).existsByEmail(testUser.getEmail());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_メールアドレス重複でエラー() {
        // Given
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.registerUser(testUser)
        );

        assertEquals("このメールアドレスは既に登録されています: " + testUser.getEmail(), exception.getMessage());
        verify(userRepository).existsByEmail(testUser.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByEmail_ユーザーが存在する() {
        // Given
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByEmail(testUser.getEmail());

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void findByEmail_ユーザーが存在しない() {
        // Given
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findByEmail(testUser.getEmail());

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void changePassword_成功() {
        // Given
        String newPassword = "newPassword123";
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.changePassword(testUser.getId(), newPassword);

        // Then
        assertNotNull(result);
        verify(userRepository).findById(testUser.getId());
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_ユーザーが存在しない() {
        // Given
        Long userId = 999L;
        String newPassword = "newPassword123";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.changePassword(userId, newPassword)
        );

        assertEquals("ユーザーが見つかりません: " + userId, exception.getMessage());
        verify(userRepository).findById(userId);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void existsByEmail_存在する() {
        // Given
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        // When
        boolean result = userService.existsByEmail(testUser.getEmail());

        // Then
        assertTrue(result);
        verify(userRepository).existsByEmail(testUser.getEmail());
    }

    @Test
    void existsByEmail_存在しない() {
        // Given
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);

        // When
        boolean result = userService.existsByEmail(testUser.getEmail());

        // Then
        assertFalse(result);
        verify(userRepository).existsByEmail(testUser.getEmail());
    }

    @Test
    void isValidPassword_有効なパスワード() {
        // Given
        String validPassword = "password123";

        // When
        boolean result = userService.isValidPassword(validPassword);

        // Then
        assertTrue(result);
    }

    @Test
    void isValidPassword_無効なパスワード() {
        // Given
        String invalidPassword = "short";

        // When
        boolean result = userService.isValidPassword(invalidPassword);

        // Then
        assertFalse(result);
    }

    @Test
    void isValidPassword_nullパスワード() {
        // When
        boolean result = userService.isValidPassword(null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValidEmail_有効なメールアドレス() {
        // Given
        String validEmail = "test@example.com";

        // When
        boolean result = userService.isValidEmail(validEmail);

        // Then
        assertTrue(result);
    }

    @Test
    void isValidEmail_無効なメールアドレス() {
        // Given
        String invalidEmail = "invalid-email";

        // When
        boolean result = userService.isValidEmail(invalidEmail);

        // Then
        assertFalse(result);
    }

    @Test
    void isValidEmail_nullメールアドレス() {
        // When
        boolean result = userService.isValidEmail(null);

        // Then
        assertFalse(result);
    }
}
