package com.hotel_service.services;

import com.hotel_service.models.User;
import com.hotel_service.models_dto.UserDTO;
import com.hotel_service.repository.UserRepository;
import com.hotel_service.stat.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
    @author Berkeshchuk
    @project App.java
    @class UserServiceMockTest
    @version 1.0.0
    @since 5/7/2025-11.45
*/
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceMockTest {

    @Mock
    UserRepository mockRepository;
    @Captor
    ArgumentCaptor<User> argumentCaptor;
    @Captor
    private ArgumentCaptor<Integer> idArgumentCaptor;
    UserService underTest;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        underTest = new UserService(mockRepository);
    }

    @DisplayName("Add new User. Success")
    @Test
    public void whenAddUserAndEmailIsUniqueThenOk(){
        //given
        UserDTO dto = UserDTO.builder()
                .name( "Alice")
                .email( "alice@mail.com")
                .password("pass")
                .mobileNumber("1234567890")
                .role(Role.USER)
                .build();
        given(mockRepository.existsByEmail(dto.getEmail())).willReturn(false);

        //when
        underTest.addUser(dto);

        //then
        then(mockRepository).should().save(argumentCaptor.capture());
        User user = argumentCaptor.getValue();
        assertEquals(dto.getName(), user.getName());
        assertNotNull(user.getCreatedOn());
        assertThat(user.getCreatedOn().isBefore(LocalDateTime.now())).isTrue();
        verify(mockRepository).save(user);
        verify(mockRepository).existsByEmail(dto.getEmail());
        verify(mockRepository, times(1)).save(user);
    }

    @DisplayName("2. Add new User - Failure: Email already exists")
    @Test
    void addUser_Failure_EmailAlreadyExists() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .id(0)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .password("pass456")
                .mobileNumber("0987654321")
                .role(Role.USER)
                .build();
        given(mockRepository.existsByEmail(userDTO.getEmail())).willReturn(true);

        // when
        ResponseEntity<?> response = underTest.addUser(userDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Email already in use");

        verify(mockRepository, times(1)).existsByEmail(userDTO.getEmail());
        verify(mockRepository, never()).save(any(User.class));
    }

    @DisplayName("3. Add new User - Failure: Repository throws exception during save")
    @Test
    void addUser_Failure_RepositoryThrowsException() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .id(0)
                .name("Bob Johnson")
                .email("bob.johnson@example.com")
                .password("pass")
                .mobileNumber("1122334455")
                .role(Role.ADMIN)
                .build();

        given(mockRepository.existsByEmail(userDTO.getEmail())).willReturn(false);
        given(mockRepository.save(any(User.class))).willThrow(new RuntimeException("Database error"));

        // when
        ResponseEntity<?> response = underTest.addUser(userDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Database error");
        verify(mockRepository, times(1)).existsByEmail(userDTO.getEmail());
        verify(mockRepository, times(1)).save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(userDTO.getEmail());
    }

    @DisplayName("4. Add new User - Success: CreatedOn is set correctly")
    @Test
    void addUser_Success_CreatedOnSetCorrectly() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .id(0)
                .name("Alice Brown")
                .email("alice.brown@example.com")
                .password("pass")
                .mobileNumber("5551234567")
                .role(Role.USER)
                .build();
        given(mockRepository.existsByEmail(userDTO.getEmail())).willReturn(false);

        // when
        underTest.addUser(userDTO);

        // then
        then(mockRepository).should().save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();

        assertNotNull(capturedUser.getCreatedOn());
        assertTrue(capturedUser.getCreatedOn().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(capturedUser.getCreatedOn().isAfter(LocalDateTime.now().minusMinutes(1))); // Ensure it's recent
        assertNull(capturedUser.getUpdatedOn());
    }

    @DisplayName("5. Add new User - Success: Role is set correctly")
    @Test
    void addUser_Success_RoleSetCorrectly() {
        // given
        UserDTO userDTO =UserDTO.builder()
                .id(0)
                .name("Charlie Green")
                .email("charlie.green@example.com")
                .password("mysecret")
                .mobileNumber("9998887766")
                .role(Role.ADMIN)
                .build();
        given(mockRepository.existsByEmail(userDTO.getEmail())).willReturn(false);

        // when
        underTest.addUser(userDTO);

        // then
        then(mockRepository).should().save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();

        assertThat(capturedUser.getRole()).isEqualTo(Role.ADMIN);
    }

    // --- Tests for updateUser(UserDTO userDTO) ---

    @DisplayName("6. Update User - Happy Path")
    @Test
    void updateUser_Success_UserUpdated() {
        // given
        int userId = 1;
        User existingUser = new User("Old Name", "old.email@example.com", "oldpass", "0001112233", Role.USER);
        existingUser.setId(userId);
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .name("New Name")
                .email("new.email@example.com")
                .password("newpass")
                .mobileNumber("4445556677")
                .role(Role.ADMIN)
                .build();

        given(mockRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        ResponseEntity<?> response = underTest.updateUser(userDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(User.class);
        User responseUser = (User) response.getBody();

        then(mockRepository).should().save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();

        assertThat(capturedUser.getId()).isEqualTo(userId);
        assertThat(capturedUser.getName()).isEqualTo(userDTO.getName());
        assertThat(capturedUser.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo(userDTO.getPassword());
        assertThat(capturedUser.getMobileNumber()).isEqualTo(userDTO.getMobileNumber());
        assertThat(capturedUser.getRole()).isEqualTo(userDTO.getRole());
        assertNotNull(capturedUser.getUpdatedOn());
        assertTrue(capturedUser.getUpdatedOn().isBefore(LocalDateTime.now().plusSeconds(1)));

        assertThat(responseUser).isEqualTo(capturedUser);

        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, times(1)).save(capturedUser);
    }

    @DisplayName("7. Update User - Failure: User not found")
    @Test
    void updateUser_Failure_UserNotFound() {
        // given
        int userId = 99;
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .name("Non Existent")
                .email("no.exist@example.com")
                .password("pass")
                .mobileNumber("123")
                .role(Role.USER)
                .build();
        given(mockRepository.findById(userId)).willReturn(Optional.empty());

        // when
        ResponseEntity<?> response = underTest.updateUser(userDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("User not found with id " + userId);

        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, never()).save(any(User.class));
    }

    @DisplayName("8. Update User - Failure: Repository throws exception during findById")
    @Test
    void updateUser_Failure_RepositoryThrowsExceptionOnFind() {
        // given
        int userId = 1;
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .name("Error User")
                .email("error@example.com")
                .password("pass")
                .mobileNumber("123")
                .role(Role.USER)
                .build();
        given(mockRepository.findById(userId)).willThrow(new RuntimeException("DB connection error"));

        // when
        ResponseEntity<?> response = underTest.updateUser(userDTO);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("DB connection error");

        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, never()).save(any(User.class));
    }

    @DisplayName("9. Update User - Success: UpdatedOn is set correctly")
    @Test
    void updateUser_Success_UpdatedOnSetCorrectly() {
        // given
        int userId = 2;
        User existingUser = new User( "Original Name", "original@example.com", "pass", "1112223344", Role.USER);
        existingUser.setId(userId);
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .name("Updated Name")
                .build(); // Only update name

        given(mockRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        underTest.updateUser(userDTO);

        // then
        then(mockRepository).should().save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();

        assertNotNull(capturedUser.getUpdatedOn());
        assertTrue(capturedUser.getUpdatedOn().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(capturedUser.getUpdatedOn().isAfter(LocalDateTime.now().minusMinutes(1)));
        assertThat(capturedUser.getName()).isEqualTo(userDTO.getName()); // name is updated
        assertThat(capturedUser.getEmail()).isEqualTo(existingUser.getEmail()); // other fields are unchanged
    }

    @DisplayName("10. Update User - Success: Only specific fields are updated")
    @Test
    void updateUser_Success_SpecificFieldsUpdated() {
        // given
        int userId = 3;
         User existingUser = new User( "Initial Name", "initial@example.com", "initialpass", "12345", Role.USER);
         existingUser.setId(userId);
         UserDTO userDTO = UserDTO.builder()
                 .id(userId)
                 .email("new.email@example.com")
                 .mobileNumber("98765")
                 .role(Role.ADMIN)
                 .build();

        given(mockRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        underTest.updateUser(userDTO);

        // then
        then(mockRepository).should().save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();

        assertThat(capturedUser.getId()).isEqualTo(userId);
        assertThat(capturedUser.getName()).isEqualTo(existingUser.getName()); // Should remain unchanged
        assertThat(capturedUser.getEmail()).isEqualTo(userDTO.getEmail()); // Should be updated
        assertThat(capturedUser.getPassword()).isEqualTo(existingUser.getPassword()); // Should remain unchanged
        assertThat(capturedUser.getMobileNumber()).isEqualTo(userDTO.getMobileNumber()); // Should be updated
        assertThat(capturedUser.getRole()).isEqualTo(userDTO.getRole()); // Should be updated
        assertNotNull(capturedUser.getUpdatedOn());
    }

    // --- Tests for getUser(int id) ---

    @DisplayName("11. Get User by ID - Happy Path")
    @Test
    void getUser_Success_UserFoundById() {
        // given
        int userId = 1;
        User existingUser = new User( "Found User", "found@example.com", "pass", "123", Role.USER);
        existingUser.setId(userId);

        given(mockRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        ResponseEntity<?> response = underTest.getUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(User.class);
        User returnedUser = (User) response.getBody();

        assertThat(returnedUser.getId()).isEqualTo(existingUser.getId());
        assertThat(returnedUser.getEmail()).isEqualTo(existingUser.getEmail());

        // Capturing the ID passed to findById, as no User object is passed to the repository.
        then(mockRepository).should().findById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(userId);

        verify(mockRepository, times(1)).findById(userId);
    }

    @DisplayName("12. Get User by ID - Failure: User not found")
    @Test
    void getUser_Failure_UserNotFoundById() {
        // given
        int userId = 99;
        given(mockRepository.findById(userId)).willReturn(Optional.empty());

        // when
        ResponseEntity<?> response = underTest.getUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("User not found with id " + userId);

        // Capturing the ID passed to findById
        then(mockRepository).should().findById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(userId);

        verify(mockRepository, times(1)).findById(userId);
    }

    @DisplayName("13. Get User by ID - Failure: Repository throws exception")
    @Test
    void getUser_Failure_RepositoryThrowsException() {
        // given
        int userId = 1;
        given(mockRepository.findById(userId)).willThrow(new RuntimeException("Server issue"));

        // when
        ResponseEntity<?> response = underTest.getUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Server issue");

        // Capturing the ID passed to findById
        then(mockRepository).should().findById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(userId);

        verify(mockRepository, times(1)).findById(userId);
    }

    // --- Tests for getUsers() ---

    @DisplayName("14. Get All Users - Happy Path: Returns list of users")
    @Test
    void getUsers_Success_ReturnsListOfUsers() {
        // given
        List<User> users = new ArrayList<>();
        users.add(createUserForMock(1, "User One", "one@example.com", "p1", "111", Role.USER));
        users.add(createUserForMock(2, "User Two", "two@example.com", "p2", "222", Role.ADMIN));
        given(mockRepository.findAll()).willReturn(users);

        // when
        ResponseEntity<?> response = underTest.getUsers();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(List.class);
        List<User> returnedUsers = (List<User>) response.getBody();

        assertThat(returnedUsers).hasSize(2);
        assertThat(returnedUsers.get(0).getEmail()).isEqualTo("one@example.com");
        assertThat(returnedUsers.get(1).getEmail()).isEqualTo("two@example.com");

        verify(mockRepository, times(1)).findAll();
    }



    @DisplayName("15. Get All Users - Happy Path: Returns empty list when no users found")
    @Test
    void getUsers_Success_ReturnsEmptyList() {
        // given
        given(mockRepository.findAll()).willReturn(Collections.emptyList());

        // when
        ResponseEntity<?> response = underTest.getUsers();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("No users found");

        verify(mockRepository, times(1)).findAll();
    }

    @DisplayName("16. Get All Users - Failure: Repository throws exception")
    @Test
    void getUsers_Failure_RepositoryThrowsException() {
        // given
        given(mockRepository.findAll()).willThrow(new RuntimeException("DB connection failed"));

        // when
        ResponseEntity<?> response = underTest.getUsers();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("DB connection failed");

        verify(mockRepository, times(1)).findAll();
    }

    // --- Tests for deleteUser(int id) ---

    @DisplayName("17. Delete User - Happy Path")
    @Test
    void deleteUser_Success_UserDeleted() {
        // given
        int userId = 1;
        User existingUser = createUserForMock(userId, "Delete Me", "delete@example.com", "pass", "123", Role.USER);
        given(mockRepository.findById(userId)).willReturn(Optional.of(existingUser));

        // when
        ResponseEntity<?> response = underTest.deleteUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        then(mockRepository).should().deleteById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(userId);

        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, times(1)).deleteById(userId);
    }

    @DisplayName("18. Delete User - Failure: User not found")
    @Test
    void deleteUser_Failure_UserNotFound() {
        // given
        int userId = 99;
        given(mockRepository.findById(userId)).willReturn(Optional.empty());

        // when
        ResponseEntity<?> response = underTest.deleteUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("User not found with id " + userId);

        then(mockRepository).should().findById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(userId);

        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, never()).deleteById(anyInt());
    }

    @DisplayName("19. Delete User - Failure: Repository throws exception during findById")
    @Test
    void deleteUser_Failure_RepositoryThrowsExceptionOnFind() {
        // given
        int userId = 1;
        given(mockRepository.findById(userId)).willThrow(new RuntimeException("DB error on find"));

        // when
        ResponseEntity<?> response = underTest.deleteUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("DB error on find");

        then(mockRepository).should().findById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(userId);

        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, never()).deleteById(anyInt());
    }

    @DisplayName("20. Delete User - Failure: Repository throws exception during deleteById")
    @Test
    void deleteUser_Failure_RepositoryThrowsExceptionOnDelete() {
        // given
        int userId = 1;
        User existingUser = createUserForMock(userId, "Problem User", "problem@example.com", "pass", "123", Role.USER);
        given(mockRepository.findById(userId)).willReturn(Optional.of(existingUser));
        doThrow(new RuntimeException("Delete failed")).when(mockRepository).deleteById(userId);

        // when
        ResponseEntity<?> response = underTest.deleteUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Delete failed");

        then(mockRepository).should().deleteById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(userId);

        verify(mockRepository, times(1)).findById(userId);
        verify(mockRepository, times(1)).deleteById(userId);
    }

    private User createUserForMock(int id, String userOne, String mail, String p1, String number, Role role) {
        User user = new User(userOne, mail, p1, number, role);
        user.setId(id);
        return user;
    }


}
