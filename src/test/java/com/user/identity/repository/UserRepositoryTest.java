package com.user.identity.repository;
import com.user.identity.repository.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    // register
    @Test
    void findByEmail_UserExists_ReturnsUser() {
        // Given
        User user = new User();
        user.setId(1); // Ensure ID is set
        user.setEmail("test@example.com");
        user.setPassword("ValidPass123!");
        user.setFirstName("John");
        user.setLastName("Doe");

        userRepository.save(user);

        // When
        Optional<User> result = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmail_UserDoesNotExist_ReturnsEmpty() {
        // When
        Optional<User> result = userRepository.findByEmail("non_existing_user@example.com");

        // Then
        assertFalse(result.isPresent());
    }
    // login
    @Test
    void findUser_UserExists_ReturnsUser() {
        // Given: Tạo một người dùng trong cơ sở dữ liệu với email và mật khẩu mã hóa
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword"); // Giả định password đã mã hóa
        userRepository.save(user);

        // When: Tìm kiếm người dùng theo email
        Optional<User> result = userRepository.findByEmail("test@example.com");

        // Then: Xác minh rằng người dùng được tìm thấy và email chính xác
        assertTrue(result.isPresent(), "User should be found");
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findUser_UserDoesNotExist_ReturnsEmpty() {
        // When: Tìm kiếm người dùng không tồn tại
        Optional<User> result = userRepository.findByEmail("non_existing_user@example.com");

        // Then: Xác minh rằng không tìm thấy người dùng
        assertFalse(result.isPresent(), "User should not be found");
    }

}