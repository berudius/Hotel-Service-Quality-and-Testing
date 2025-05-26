package com.hotel_service;

/*
    @author Berkeshchuk
    @project App.java
    @class IntegrationTests
    @version 1.0.0
    @since 5/26/2025-10.27
*/


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel_service.models.User;
import com.hotel_service.models_dto.UserDTO;
import com.hotel_service.repository.UserRepository;
import com.hotel_service.stat.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() {

        users.add( new User( "Alice", "alice@example.com", "password456", "2345678901", Role.USER));
        users.add( new User( "Bob", "bob@example.com", "password456", "1345678901", Role.USER));
        users.add( new User( "Norton", "norton@example.com", "password456", "2344678901", Role.USER));
        userRepository.saveAll(users);
    }

    @AfterEach
    void tearsDown(){
        userRepository.deleteAll();
    }


    @Test
    //Create user — Happy Path
    void itShouldCreateNewUser() throws Exception {
        // given
        UserDTO request =  UserDTO.builder()
                .name("Tom")
                .email("tom@example.com")
                .password("secret123")
                .mobileNumber("1234567890")
                .role(Role.USER)
                .build();
        // when
        ResultActions perform = mockMvc.perform(post("/users/add_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        User user = userRepository.findAll()
                .stream()
                .filter(it -> it.getEmail().equals(request.getEmail()))
                .findFirst().orElse(null);

        perform.andExpect(status().isOk());
        assertTrue(userRepository.existsByEmail(request.getEmail()));
        assertNotNull(user);
        assertThat(user.getName()).isEqualTo(request.getName());
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getMobileNumber()).isEqualTo(request.getMobileNumber());
        assertNull(user.getUpdatedOn());
    }

    @Test
    //Create user - email alreay in use (negative)
    void itShouldFailToCreateUserWhenEmailExists() throws Exception {
        // given
        User existingUser = new User("Jane", "jane@example.com", "123", "9876543210", Role.USER);
        userRepository.save(existingUser);
        UserDTO request = UserDTO.builder()
                .name("Jane2")
                .email("jane@example.com")
                .password("abc")
                .mobileNumber("1111111111")
                .role(Role.USER)
                .build();


        // when
        ResultActions perform = mockMvc.perform(post("/users/add_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        List<User> localUsers = userRepository.findAll()
                .stream()
                .filter(it -> it.getEmail().equals(request.getEmail()))
                .toList();

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email already in use")));
        assertFalse(userRepository.existsByName("Jane2"));
        assertNotNull(localUsers);
        assertEquals(1, localUsers.size());
        assertEquals("Jane", localUsers.getFirst().getName());
    }

    @Test
//Update user — Happy Path
    void itShouldUpdateExistingUser() throws Exception {
        // given
        User user = userRepository.save(
                new User("Jack", "jack@example.com", "123", "9999999999", Role.USER)
        );
        UserDTO updated = UserDTO.builder()
                .id(user.getId())
                .name("Jackie")
                .email("jack@example.com")
                .password("newpass")
                .mobileNumber("9999999999")
                .role(Role.USER)
                .build();


        // when
        ResultActions perform = mockMvc.perform(put("/users/update_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)));

        // then
        User modified = userRepository.findById(user.getId()).orElse(null);

        perform.andExpect(status().isOk());
        assertNotNull(modified);
        assertEquals("Jackie", modified.getName());
        assertEquals("newpass", modified.getPassword());
        assertEquals("jack@example.com", modified.getEmail());
        assertEquals("9999999999", modified.getMobileNumber());
    }

    @Test
//Update user - user not found (negative)
    void itShouldFailToUpdateNonExistentUser() throws Exception {
        // given
        UserDTO nonExistingUser = UserDTO.builder()
                .id(9999)
                .name("Ghost")
                .email("ghost@example.com")
                .password("123")
                .mobileNumber("1234567890")
                .role(Role.USER)
                .build();

        // when
        ResultActions perform = mockMvc.perform(put("/users/update_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nonExistingUser)));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User not found")));
        assertFalse(userRepository.existsByEmail("ghost@example.com"));
    }

    @Test
    //Get user by ID — Happy Path
    void itShouldGetUserById() throws Exception {
        // given
        User user = userRepository.save(new User("Linda", "linda@example.com", "321", "2223334444", Role.USER));

        // when
        ResultActions perform = mockMvc.perform(get("/users/" + user.getId()));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Linda"))
                .andExpect(jsonPath("$.email").value("linda@example.com"))
                .andExpect(jsonPath("$.mobileNumber").value("2223334444"));
    }

    @Test
    //Get user - user not found (negative)
    void itShouldReturnErrorForNonExistentUser() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/users/99999"));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User not found")));
    }

    @Test
    //Delete user — Happy Path
    void itShouldDeleteUser() throws Exception {
        // given
        User user = userRepository.save(new User("Tommy", "tommy@example.com", "pass", "1112223333", Role.USER));

        // when
        ResultActions perform = mockMvc.perform(delete("/users/delete_user/" + user.getId()));

        // then
        perform.andExpect(status().isOk());
        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    //Delete user - user not found (negative)
    void itShouldFailToDeleteNonExistentUser() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(delete("/users/delete_user/99999"));

        // then
        User user = userRepository.findById(99999).orElse(null);

        perform.andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User not found")));
        assertNull(user);
        assertFalse(userRepository.existsById(99999));
    }

    @Test
//Get all users — Happy Path
    void itShouldReturnListOfUsers() throws Exception {
        // given
        userRepository.save(new User("Mike", "mike@example.com", "abc", "4445556666", Role.USER));
        userRepository.save(new User("Sara", "sara@example.com", "xyz", "5556667777", Role.USER));

        // when
        ResultActions perform = mockMvc.perform(get("/users"));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[0].email").isNotEmpty());
    }

    @Test
    //Get all users - no users found (negative)
    void itShouldReturnErrorWhenNoUsers() throws Exception {
        // given
        userRepository.deleteAll();

        // when
        ResultActions perform = mockMvc.perform(get("/users"));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("No users found")));
    }

}
