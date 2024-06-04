package com.vpactually.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.users.UserUpdateDTO;
import com.vpactually.entities.User;
import com.vpactually.repositories.UserRepository;
import com.vpactually.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;

    private User anotherUser;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        anotherUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
        userRepository.save(anotherUser);
    }
    @Test
    public void testShow() throws Exception {



        var request = get("/api/users/{id}", testUser.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testUser.getName()),
                v -> v.node("email").isEqualTo(testUser.getEmail()),
                v -> v.node("id").isEqualTo(testUser.getId())
        );
    }

    @Test
    public void testIndex() throws Exception {

        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testCreate() throws Exception {
        var newUser = Instancio.of(modelGenerator.getUserModel()).create();

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newUser));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(newUser.getEmail()).orElseThrow();

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(newUser.getName());
        assertThat(user.getEmail()).isEqualTo(newUser.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        var update = new UserUpdateDTO(
                "New First Name", null);

        var updateReq = put("/api/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(update));

        mockMvc.perform(updateReq)
                .andExpect(status().isOk());

        var user = userRepository.findByEmail(testUser.getEmail()).orElseThrow();

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("New First Name");
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    public void testDelete() throws Exception {
        var delReq = delete("/api/users/" + testUser.getId());

        mockMvc.perform(delReq).andExpect(status().isNoContent());
        assertFalse(userRepository.findByEmail(testUser.getEmail()).isPresent());
    }

}
