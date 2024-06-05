package com.vpactually.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.tasks.TaskCreateDTO;
import com.vpactually.dto.tasks.TaskUpdateDTO;
import com.vpactually.entities.Task;
import com.vpactually.entities.TaskStatus;
import com.vpactually.entities.User;
import com.vpactually.repositories.TaskRepository;
import com.vpactually.repositories.TaskStatusRepository;
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
public class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;
    private TaskStatus testTaskStatus;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        testUser = Instancio.of(modelGenerator.getUserModel()).create();

        taskStatusRepository.save(testTaskStatus);
        userRepository.save(testUser);

        testTask.setTaskStatus(testTaskStatus);
        testTask.setAssignee(testUser);
        taskRepository.save(testTask);
    }

    @Test
    public void showTest() throws Exception {
        var response = mockMvc.perform(get("/api/tasks/" + testTask.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThatJson(response.getContentAsString()).and(
                v -> v.node("assigneeId").isEqualTo(testUser.getId()),
                v -> v.node("taskStatus").isEqualTo(testTaskStatus.getSlug())
        );
    }

    @Test
    public void indexTest() throws Exception {
        var response = mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response)
                .contains(String.valueOf(testTask.getAssignee().getId()))
                .contains(testTask.getDescription())
                .contains(testTask.getTaskStatus().getSlug());
    }

    @Test
    public void createTest() throws Exception {
        taskRepository.delete(testTask);

        var dto = new TaskCreateDTO();
        dto.setTitle(testTask.getTitle());
        dto.setDescription(testTask.getDescription());
        dto.setAssigneeId(testTask.getAssignee().getId());
        dto.setTaskStatus(testTask.getTaskStatus().getSlug());

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var task = taskRepository.findByTitle(dto.getTitle()).orElseThrow();

        assertThat(task.getTitle()).isEqualTo(dto.getTitle());
        assertThat(task.getDescription()).isEqualTo(dto.getDescription());
        assertThat(task.getAssignee().getId()).isEqualTo(dto.getAssigneeId());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(dto.getTaskStatus());

    }

    @Test
    public void updateTest() throws Exception {
        var dto = new TaskUpdateDTO("New Title", "New Content", "to_review",
                null, null);

        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        var task = taskRepository.findByTitle(dto.getTitle().get()).orElseThrow();

        assertThat(task.getTitle()).isEqualTo(dto.getTitle().get());
        assertThat(task.getDescription()).isEqualTo(dto.getDescription().get());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(dto.getTaskStatus().get());
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
        assertFalse(taskRepository.findByTitle(testTask.getTitle()).isPresent());
    }
}
