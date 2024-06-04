package com.vpactually.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.taskStatuses.TaskStatusUpdateDTO;
import com.vpactually.entities.TaskStatus;
import com.vpactually.repositories.TaskStatusRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskStatusesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private TaskStatus testTaskStatus;

    @BeforeEach
    public void setUp() {
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);
    }

    @Test
    public void showTest() throws Exception {
        var response1 = mockMvc.perform(get("/api/task-statuses/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response1.getContentAsString()).contains("Draft");
        assertThat(response1.getContentAsString()).contains("draft");
    }

    @Test
    public void indexTest() throws Exception {
        var response = mockMvc.perform(get("/api/task-statuses"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response.getContentAsString()).contains("Draft");
        assertThat(response.getContentAsString()).contains("to_review");
        assertThat(response.getContentAsString()).contains("to_be_fixed");
        assertThat(response.getContentAsString()).contains("ToPublish");
        assertThat(response.getContentAsString()).contains("Published");
    }

    @Test
    public void createTest() throws Exception {
        var newStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();

        mockMvc.perform(post("/api/task-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newStatus)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var status = taskStatusRepository.findBySlug(newStatus.getSlug()).orElseThrow();
        assertThat(status.getName()).isEqualTo(newStatus.getName());
        assertThat(status.getSlug()).isEqualTo(newStatus.getSlug());
    }

    @Test
    public void updateTest() throws Exception {
        var dto = new TaskStatusUpdateDTO("New Name", "new_slug");
        mockMvc.perform(put("/api/task-statuses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var taskStatus = taskStatusRepository.findBySlug(dto.getSlug().get()).orElseThrow();
        assertThat(taskStatus.getSlug()).isEqualTo(dto.getSlug().get());
        assertThat(taskStatus.getName()).isEqualTo(dto.getName().get());
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/task-statuses/" + testTaskStatus.getId()))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertFalse(taskStatusRepository.findBySlug(testTaskStatus.getSlug()).isPresent());
    }
}
