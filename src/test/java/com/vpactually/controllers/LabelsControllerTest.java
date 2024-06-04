package com.vpactually.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.labels.LabelCreateDTO;
import com.vpactually.dto.labels.LabelUpdateDTO;
import com.vpactually.entities.Label;
import com.vpactually.entities.Task;
import com.vpactually.entities.TaskStatus;
import com.vpactually.entities.User;
import com.vpactually.repositories.LabelRepository;
import com.vpactually.repositories.TaskRepository;
import com.vpactually.repositories.TaskStatusRepository;
import com.vpactually.repositories.UserRepository;
import com.vpactually.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
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
public class LabelsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private Label testLabel;

    private Task testTask;

    private TaskStatus testTaskStatus;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        testUser = Instancio.of(modelGenerator.getUserModel()).create();

        taskStatusRepository.save(testTaskStatus);
        userRepository.save(testUser);

        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
        taskRepository.save(testTask);
        labelRepository.save(testLabel);
    }

    @Test
    public void showTest() throws Exception {
        var response = mockMvc.perform(get("/api/labels/" + testLabel.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    public void indexTest() throws Exception {
        var response = mockMvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(response).contains(testLabel.getName());
    }

    @Test
    public void createTest() throws Exception {
        var dto = new LabelCreateDTO();
        dto.setName("new Label");

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var label = labelRepository.findByName(dto.getName()).orElseThrow();
        assertThat(label.getName()).isEqualTo(dto.getName());
    }

    @Test
    public void updateTest() throws Exception {
        var dto = new LabelUpdateDTO(JsonNullable.of("new Label1"));

        mockMvc.perform(put("/api/labels/" + testLabel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk());

        assertThat(labelRepository.findByName(dto.getName().get())).isPresent();
    }

    @Test
    public void deleteTest() throws Exception {

        mockMvc.perform(delete("/api/labels/" + testLabel.getId()))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertFalse(labelRepository.findByName(testLabel.getName()).isPresent());
    }

}
