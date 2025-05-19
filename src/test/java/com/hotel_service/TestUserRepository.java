package com.hotel_service;

/*
    @author Berkeshchuk
    @project App.java
    @class TestUserRepository
    @version 1.0.0
    @since 5/19/2025-20.19
*/

import com.hotel_service.models.User;
import com.hotel_service.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestUserRepository {

    @Autowired
    UserRepository underTest;

    @Autowired
    EntityManager entityManager;

    @BeforeAll
    void beforeAll() {
        underTest.deleteAll();
    }

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        User user1 = createTestUser("Alice", "alice@example.com", "1111111111", "###test");
        User user2 = createTestUser("Bob", "bob@example.com", "2222222222", "###test");
        User user3 = createTestUser("Charlie", "charlie@example.com", "3333333333", "###test");
        underTest.saveAll(List.of(user1, user2, user3));
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testSetShouldContain_3_Records_ToTest() {
        List<User> testUsers = underTest.findAll().stream()
                .filter(u -> u.getPassword().contains("###test"))
                .toList();
        assertEquals(3, testUsers.size());
    }

    @Test
    void shouldAssignIdToNewUser() {
        // given
        User newUser = createTestUser("David", "david@example.com", "4444444444", "###test");
        newUser.setId(0);
        // when
        User saved = underTest.save(newUser);
        // then
        assertNotEquals(0, saved.getId());
        assertNotNull(saved.getId());
    }

    @Test
    void canFindUserByEmail() {
        // given
        String email = "alice@example.com";

        // when
        User user = underTest.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().orElse(null);

        // then
        assertNotNull(user);
        assertEquals("Alice", user.getName());
    }

    @Test
    void canDeleteUserById() {
        // given
        User user = underTest.findAll().stream()
                .filter(u -> u.getEmail().equals("bob@example.com"))
                .findFirst().orElse(null);
        assertNotNull(user);
        // when
        underTest.deleteById(user.getId());
        // then
        assertFalse(underTest.findById(user.getId()).isPresent());
    }

    @Test
    void canUpdateUserName() {
        // given
        User user = underTest.findAll().stream()
                .filter(u -> u.getEmail().equals("charlie@example.com"))
                .findFirst().orElse(null);
        assertNotNull(user);
        user.setName("Chuck");
        // when
        User updated = underTest.save(user);
        // then
        assertEquals("Chuck", updated.getName());
    }

    @Test
    void canFindUsersByNameContaining() {
        // given
        String namePart = "li"; // Має знайти "Alice" та "Charlie"
        // when
        List<User> foundUsers = underTest.findByNameContaining(namePart);
        // then
        assertTrue(foundUsers.stream().anyMatch(user -> user.getName().equals("Alice")));
        assertTrue(foundUsers.stream().anyMatch(user -> user.getName().equals("Charlie")));
        assertFalse(foundUsers.stream().anyMatch(user -> user.getName().equals("Bob")));
    }


    @Test
    void deletingAllUsersAndCheckingCount() {
        // given
        // setUp вже додав 3 користувачів

        // when
        underTest.deleteAll();

        // then
        assertEquals(0, underTest.count());
    }

    @Test
    void canCountAllUsers() {
        // given
        long initialCount = underTest.count();
        User newUser1 = createTestUser("Катерина", "kate1@example.com", "1122334455", "###test");
        underTest.save(newUser1);
        User newUser2 = createTestUser("Іван", "ivan1@example.com", "6677889900", "###test");
        underTest.save(newUser2);
        // when
        long currentCount = underTest.count();
        // then
        assertEquals(initialCount + 2, currentCount);
    }


    @Test
    void canFindUsersCreatedWithinSpecificDateRange() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);

        User userInRange1 = createTestUser("username", "inrange1@example.com", "1111111112", "###test");
        userInRange1.setCreatedOn(now.minusHours(12));
        underTest.save(userInRange1);

        User userInRange2 = createTestUser("username 2", "inrange2@example.com", "2222222224", "###test");
        userInRange2.setCreatedOn(yesterday.plusHours(5));
        underTest.save(userInRange2);

        User userOutOfRange = createTestUser("user name 3", "outofrange@example.com", "3333333335", "###test");
        userOutOfRange.setCreatedOn(now.plusDays(2));
        underTest.save(userOutOfRange);

        // when
        List<User> foundUsers = underTest.findByCreatedOnBetween(yesterday, now);

        // then
        assertTrue(foundUsers.stream().anyMatch(user -> user.getEmail().equals("inrange1@example.com")));
        assertTrue(foundUsers.stream().anyMatch(user -> user.getEmail().equals("inrange2@example.com")));
        assertFalse(foundUsers.stream().anyMatch(user -> user.getEmail().equals("outofrange@example.com")));
    }


    @Test
    void savedUserShouldBeRetrievable() {
        // given
        User user = createTestUser("Grace", "grace@example.com", "7777777777", "###test");
        underTest.save(user);
        // when
        User fromDb = underTest.findAll().stream()
                .filter(u -> u.getEmail().equals("grace@example.com"))
                .findFirst().orElse(null);
        // then
        assertNotNull(fromDb);
        assertEquals("Grace", fromDb.getName());
    }

    @Test
    void findAllByOrderByNameAsc_ShouldReturnUsersSortedByName() {
        // given
        List<User> allUsers = underTest.findAll();

        // when
        List<User> sortedUsersFromRepo = underTest.findAllByOrderByNameAsc();

        // then
        List<User> locallySorted = allUsers.stream()
                .sorted((u1, u2) -> u1.getName().compareTo(u2.getName()))
                .toList();

        assertEquals(locallySorted, sortedUsersFromRepo, "The repository should return users sorted by name ascending");
    }



    private User createTestUser(String name, String email, String mobile, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setMobileNumber(mobile);
        user.setPassword(password);
        user.setRole("USER");
        user.setCreatedOn(LocalDateTime.now());
        return user;
    }
}

