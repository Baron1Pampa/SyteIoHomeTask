package com.syte.io.semyonm;

import com.syte.io.semyonm.TodoList.GetTaskResponse;
import com.syte.io.semyonm.TodoList.Task;
import com.syte.io.semyonm.TodoList.TaskState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToDoListApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    ProtobufHttpMessageConverter protobufHttpMessageConverter = new ProtobufJsonFormatHttpMessageConverter();
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    private final RestTemplate restTemplate = new RestTemplate(List.of(protobufHttpMessageConverter, mappingJackson2HttpMessageConverter));
    @LocalServerPort
    private int port;

    public ApplicationIntegrationTest() {
    }

    @Test
    public void  whenTaskCreated_theReturnCreatedTask() {
        String title = "This is a Title";
        String message = "This is a task message";
        TodoList.CreateTaskRequest request = TodoList.CreateTaskRequest.newBuilder()
                .setTitle(title)
                .setMessage(message)
                .build();

        ResponseEntity<TodoList.CreateTaskResponse> response = restTemplate.postForEntity(getBaseTaskUrl(), request, TodoList.CreateTaskResponse.class);
        Task createdTask = response.getBody().getTask();
        assertNotNull(createdTask);
        assertEquals(message, createdTask.getMessage());
        assertEquals(title, createdTask.getTitle() );
        assertNotNull(createdTask.getId());
        assertEquals(TaskState.NEW, createdTask.getTaskState());
    }





    private String getSingleTaskUrl(String taskId) {
        return getBaseTaskUrl()+ "/" + taskId;
    }

    private String getBaseTaskUrl() {
        return "http://localhost:" + port + "/tasks";
    }
}