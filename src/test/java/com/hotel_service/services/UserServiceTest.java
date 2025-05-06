package com.hotel_service.services;

/*
    @author Berkeshchuk
    @project App.java
    @class UserServiceTest
    @version 1.0.0
    @since 5/6/2025-11.11
*/
import java.util.List;
import java.util.Objects;

import com.hotel_service.models.User;
import com.hotel_service.models_dto.UserDTO;
import com.hotel_service.repository.UserRepository;
import com.hotel_service.stat.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService underTest;

    @AfterEach
    void  tearDown() {
        repository.deleteAll();
    }


    @Test
    void addUser_shouldReturnOk_whenValidUserIsAdded() {
        // given
        UserDTO userDto = UserDTO.builder()
                .name( "Alice")
                .email( "alice@mail.com")
                .password("pass")
                .mobileNumber("1234567890")
                .role(Role.USER)
                .build();
        // when
        ResponseEntity<?> response = underTest.addUser(userDto);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(repository.existsById(Integer.valueOf(Objects.requireNonNull(response.getBody()).toString())));
    }

    @Test
    void getUser_shouldReturnUser_whenIdExists() {
        // given
        User user = new User("Bob", "bob@mail.com", "pass", "0987654321", Role.ADMIN);
        int userId = repository.save(user).getId();

        // when
        ResponseEntity<?> response = underTest.getUser(userId);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof User);
    }

    @Test
    void getUser_shouldReturnBadRequest_whenIdDoesNotExist() {
        // given

        // when
        ResponseEntity<?> response = underTest.getUser(999);

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("not found"));
    }

    @Test
    void getUsers_shouldReturnUserList_whenUsersExist() {

        // given
        User user = new User( "Charlie", "charlie@mail.com", "pass", "123123123", Role.USER);
        repository.save(user).getId();

        // when
        ResponseEntity<?> response = underTest.getUsers();

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((List<?>) response.getBody()).size() > 0);
    }

    @Test
    void getUsers_shouldReturnBadRequest_whenNoUsersFound() {
        // given

        // when
        ResponseEntity<?> response = underTest.getUsers();

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("No users"));
    }

    @Test
    void updateUser_shouldReturnOk_whenUserExists() {
        // given
        User user = new User( "Dave", "dave@mail.com", "pass", "000111000", Role.USER);
        int userId = repository.save(user).getId();
        UserDTO updatedDto = new UserDTO(userId, "David", "dave@mail.com", "pass", "000111000", Role.ADMIN);

        // when
        ResponseEntity<?> response = underTest.updateUser(updatedDto);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("David", ((User) response.getBody()).getName());
    }

    @Test
    void updateUser_shouldReturnBadRequest_whenUserDoesNotExist() {
        // given
        UserDTO dto = new UserDTO(777, "Ghost", "ghost@mail.com", "pass", "000", Role.USER);

        // when
        ResponseEntity<?> response = underTest.updateUser(dto);

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_shouldReturnOk_whenUserExists() {
        // given
        User user = new User( "Eve", "eve@mail.com", "pass", "1111111111", Role.USER);
        int userId = repository.save(user).getId();

        // when
        ResponseEntity<?> response = underTest.deleteUser(userId);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(repository.findById(5).isPresent());
    }

    @Test
    void deleteUser_shouldReturnBadRequest_whenUserDoesNotExist() {
        // given

        // when
        ResponseEntity<?> response = underTest.deleteUser(888);

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void getUser_shouldReturnCorrectUserById() {
        // given
        User user = new User( "Frank", "frank@mail.com", "pass", "222333444", Role.USER);
        int userId = repository.save(user).getId();

        // when
        ResponseEntity<?> response = underTest.getUser(userId);

        // then
        assertNotNull(response);
        User found = (User) response.getBody();
        assertEquals("Frank", found.getName());
        assertEquals("frank@mail.com", found.getEmail());
    }


    @Test
    void whenGetUsers_ThenUsersListIsReturned() {
        // given
        User user1 = new User( "John Doe", "john@example.com", "password123", "1234567890", Role.USER);
        User user2 = new User( "Jane Doe", "jane@example.com", "password123", "0987654321", Role.ADMIN);
        repository.save(user1);
        repository.save(user2);

        // when
        ResponseEntity<?> response = underTest.getUsers();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).size() > 0);
    }

    @Test
    void whenGetUser_ThenReturnUserWithValidId() {
        // given
        User user = new User( "John Doe", "john@example.com", "password123", "1234567890", Role.USER);
        int userId = repository.save(user).getId();

        // when
        ResponseEntity<?> response = underTest.getUser(userId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", ((User) response.getBody()).getName());
    }

    @Test
    void whenGetUser_ThenReturnBadRequestForInvalidId() {
        // given
        // No users saved in the repository

        // when
        ResponseEntity<?> response = underTest.getUser(999);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((String) response.getBody()).contains("User not found with id"));
    }

    @Test
    void whenAddUser_ThenUserIsCreated() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .name( "John Doe")
                .email( "john@example.com")
                .password("password123")
                .mobileNumber("1234567890")
                .role(Role.USER)
                .build();

        // when
        ResponseEntity<?> response = underTest.addUser(userDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(repository.existsById(Integer.valueOf(Objects.requireNonNull(response.getBody()).toString())));
    }

    @Test
    void whenAddUser_ThenReturnInternalServerErrorOnException() {
        // given
        UserDTO userDTO = null; // Invalid input to trigger exception

        // when
        ResponseEntity<?> response = underTest.addUser(userDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void whenUpdateUser_ThenUserIsUpdated() {
        // given
        User user = new User("John Doe", "john@example.com", "password123", "1234567890", Role.USER);
        int userId = repository.save(user).getId();
        UserDTO updatedDTO = new UserDTO(userId, "John Smith", "john.smith@example.com", "newpassword123", "0987654321", Role.ADMIN);

        // when
        ResponseEntity<?> response = underTest.updateUser(updatedDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User updatedUser = (User) response.getBody();
        assertEquals("John Smith", updatedUser.getName());
        assertEquals("john.smith@example.com", updatedUser.getEmail());
    }

    @Test
    void whenUpdateUser_ThenReturnBadRequestForNonExistentUser() {
        // given
        UserDTO updatedDTO = new UserDTO(999, "John Smith", "john.smith@example.com", "newpassword123", "0987654321", Role.ADMIN);

        // when
        ResponseEntity<?> response = underTest.updateUser(updatedDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((String) response.getBody()).contains("User not found with id"));
    }

    @Test
    void whenDeleteUser_ThenUserIsDeleted() {
        // given
        User user = new User( "John Doe", "john@example.com", "password123", "1234567890", Role.USER);
        int userId = repository.save(user).getId();

        // when
        ResponseEntity<?> response = underTest.deleteUser(userId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(repository.existsById(1));
    }

    @Test
    void whenDeleteUser_ThenReturnBadRequestForNonExistentUser() {
        // given
        // No users saved

        // when
        ResponseEntity<?> response = underTest.deleteUser(999);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((String) response.getBody()).contains("User not found with id"));
    }

    @Test
    void whenAddUserWithInvalidData_ThenReturnInternalServerError() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .name(null)
                .email("")
                .password("")
                .mobileNumber("")
                .role( null)
                .build();
        // when
        ResponseEntity<?> response = underTest.addUser(userDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void whenAddUserWithMissingFields_ThenReturnInternalServerError() {
        // given
        UserDTO userDTO = new UserDTO(1, "", "john@example.com", "password123", "1234567890", Role.USER);

        // when
        ResponseEntity<?> response = underTest.addUser(userDTO);

        // then
//        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void whenGetUsers_ThenEmptyListIsReturnedWhenNoUsersExist() {
        // given
        // No users in the repository

        // when
        ResponseEntity<?> response = underTest.getUsers();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No users found", response.getBody());
    }

    @Test
    void whenAddUserWithValidData_ThenUserIsSavedSuccessfully() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .name( "Alice")
                .email( "alice@example.com")
                .password("password456")
                .mobileNumber("2345678901")
                .role(Role.USER)
                .build();

        // when
        ResponseEntity<?> response = underTest.addUser(userDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(repository.existsById(Integer.valueOf(Objects.requireNonNull(response.getBody()).toString())));
    }

    @Test
    void whenUpdateUser_ThenUserIsUpdatedWithNewData() {
        // given
        User user = new User( "Alice", "alice@example.com", "password456", "2345678901", Role.USER);
        int userId = repository.save(user).getId();
        UserDTO updatedDTO = new UserDTO(userId, "Alice Updated", "alice.updated@example.com", "newpassword123", "9876543210", Role.ADMIN);

        // when
        ResponseEntity<?> response = underTest.updateUser(updatedDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User updatedUser = (User) response.getBody();
        assertEquals("Alice Updated", updatedUser.getName());
        assertEquals("alice.updated@example.com", updatedUser.getEmail());
    }

    @Test
    void whenUpdateUserWithNoExistedUser_ThenReturnBadRequest() {
        // given
        UserDTO invalidUserDTO = new UserDTO(1, "", "", "", "", null); // Invalid user DTO

        // when
        ResponseEntity<?> response = underTest.updateUser(invalidUserDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void whenDeleteUser_ThenReturnInternalServerErrorForNonExistentUser() {
        // given
        // No users in the repository

        // when
        ResponseEntity<?> response = underTest.deleteUser(999);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((String) response.getBody()).contains("User not found with id"));
    }

    @Test
    void whenDeleteUser_ThenReturnBadRequestForUserNotFound() {
        // given
        int invalidUserId = 999;  // Неіснуючий користувач

        // when
        ResponseEntity<?> response = underTest.deleteUser(invalidUserId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found with id"));
    }


    @Test
    void whenAddUserWithExistingId_ThenReturnBadRequest() {
        // given
        User user = new User( "Alice", "alice@example.com", "password456", "2345678901", Role.USER);
        repository.save(user);
        UserDTO duplicateDTO = new UserDTO(1, "Alice Duplicate", "alice.duplicate@example.com", "newpassword456", "2345678902", Role.USER);

        // when
        ResponseEntity<?> response = underTest.addUser(duplicateDTO);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void whenGetUser_ThenReturnBadRequestForUserNotFound() {
        // given
        int invalidUserId = 999;  // Неіснуючий користувач

        // when
        ResponseEntity<?> response = underTest.getUser(invalidUserId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found with id"));
    }


    @Test
    void whenGetUser_ThenReturnBadRequestForNonExistentUser() {
        // given
        // No users in the repository

        // when
        ResponseEntity<?> response = underTest.getUser(100);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((String) response.getBody()).contains("User not found with id"));
    }


}
