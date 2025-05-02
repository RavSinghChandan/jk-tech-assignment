git stpackage com.jktech.document_management.repository;

import com.jktech.document_management.model.User;
import com.jktech.document_management.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(Role.USER);
        testUser = userRepository.save(testUser);
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        Optional<User> user = userRepository.findByEmail("test@example.com");
        
        assertTrue(user.isPresent());
        assertEquals(testUser.getId(), user.get().getId());
        assertEquals(testUser.getEmail(), user.get().getEmail());
    }

    @Test
    void findByEmail_ShouldReturnEmptyForInvalidEmail() {
        Optional<User> user = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(user.isPresent());
    }

    @Test
    void existsByEmail_ShouldReturnTrueForExistingEmail() {
        boolean exists = userRepository.existsByEmail("test@example.com");
        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalseForNonExistingEmail() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        assertFalse(exists);
    }

    @Test
    void save_ShouldCreateNewUser() {
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("password");
        newUser.setRole(Role.USER);

        User savedUser = userRepository.save(newUser);
        
        assertNotNull(savedUser.getId());
        assertEquals(newUser.getEmail(), savedUser.getEmail());
    }

    @Test
    void save_ShouldUpdateExistingUser() {
        testUser.setPassword("newpassword");
        User updatedUser = userRepository.save(testUser);
        
        assertEquals(testUser.getId(), updatedUser.getId());
        assertEquals("newpassword", updatedUser.getPassword());
    }
} 