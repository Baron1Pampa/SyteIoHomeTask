package com.syte.io.semyonm;

import com.google.protobuf.util.JsonFormat;
import com.syte.io.semyonm.TodoList.GetTaskResponse;
import com.syte.io.semyonm.TodoList.Task;
import com.syte.io.semyonm.TodoList.TaskState;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

@SuppressWarnings("SameParameterValue")
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToDoListApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    @SuppressWarnings({"unused", "deprecation"})
//In real life I wouldn't use deprecated functionality, unless there is a very good reason, right now I allow myself - for simplicity :)
    @LocalServerPort
    private int port;

    public ApplicationIntegrationTest() {
    }

    @Test
    public void whenTaskCreated_thenReturnCreatedTask() throws IOException {
        clearData();
        String title = "This is a Title";
        String message = "This is a task message";
        TodoList.CreateTaskResponse createTaskResponse = createTaskWithPost(title, message);
        Task createdTask = createTaskResponse.getTask();
        assertNotNull(createdTask);
        assertEquals(message, createdTask.getMessage());
        assertEquals(title, createdTask.getTitle());
        assertNotNull(createdTask.getId());
        assertEquals(TaskState.NEW, createdTask.getTaskState());
    }

    @Test
    public void whenNoTaskFound_thenThrow404Code() throws IOException {
        clearData();
        getTaskWithRequiredStatus(UUID.randomUUID().toString(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenTaskExists_thenReturnTaskById() throws IOException {
        clearData();
        String title = "This is a Title";
        String message = "This is a task message";
        Task task = createTaskWithPost(title, message).getTask();
        GetTaskResponse getTaskResponse = getTaskWithRequiredStatus(task.getId(), HttpStatus.OK);
        assertEquals(task, getTaskResponse.getTask());
    }


    @Test
    public void whenTasksExists_thenReturnAllTasks() throws IOException, URISyntaxException {
        clearData();
        List<Task> expectedTasks = createTasks(3);
        TodoList.ListTasksResponse listTasksResponse = listTasksWithRequiredStatus(0, 10, HttpStatus.OK);
        int nextOffset = listTasksResponse.getNextOffset();
        List<Task> actualTasks = listTasksResponse.getTasksList();
        Task[] tasksAsArray = new Task[expectedTasks.size()];
        tasksAsArray = expectedTasks.toArray(tasksAsArray);
        assertThat(actualTasks, hasSize(expectedTasks.size()));
        assertThat(actualTasks, hasItems(tasksAsArray));
        assertThat(nextOffset, is(-1));
    }

    @Test
    public void whenTasksExists_thenReturnAllTasksPaginated() throws IOException, URISyntaxException {
        clearData();
        List<Task> expectedTasks = createTasks(10);
        expectedTasks.sort(new TaskByIdComparator());
        TodoList.ListTasksResponse listTasksResponse = listTasksWithRequiredStatus(0, 2, HttpStatus.OK);
        int nextOffset = listTasksResponse.getNextOffset();
        List<Task> actualTasks = listTasksResponse.getTasksList();
        assertThat(actualTasks, hasSize(2));
        assertThat(actualTasks, hasItems(expectedTasks.get(0), expectedTasks.get(1)));
        assertThat(nextOffset, is(2));
    }

    @Test
    public void whenTasksExists_thenReturnAllTasksPaginatedWithNonZeroOffset() throws IOException, URISyntaxException {
        clearData();
        List<Task> expectedTasks = createTasks(10);
        expectedTasks.sort(new TaskByIdComparator());
        TodoList.ListTasksResponse listTasksResponse = listTasksWithRequiredStatus(2, 3, HttpStatus.OK);
        int nextOffset = listTasksResponse.getNextOffset();
        List<Task> actualTasks = listTasksResponse.getTasksList();
        assertThat(actualTasks, hasSize(3));
        assertThat(actualTasks, hasItems(expectedTasks.get(2), expectedTasks.get(3), expectedTasks.get(4)));
        assertThat(nextOffset, is(5));
    }

    @Test
    public void whenNoTasksExist_thenReturnEmptyList() throws URISyntaxException, IOException {
        clearData();
        TodoList.ListTasksResponse listTasksResponse = listTasksWithRequiredStatus(0, 2, HttpStatus.OK);
        int nextOffset = listTasksResponse.getNextOffset();
        assertThat(nextOffset, is(-1));
        List<Task> actualTasks = listTasksResponse.getTasksList();
        assertThat(actualTasks, hasSize(0));
    }

    @Test
    public void whenListTaskWithIllegalOffset_thenThrow400Error() throws URISyntaxException, IOException {
        clearData();
        listTasksWithRequiredStatus(-1, 2, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenTaskUpdated_thenReturnUpdatedTask() throws IOException {
        clearData();
        String title = "This is a Title to Update task";
        String message = "This is a task message to be updated";
        TodoList.CreateTaskResponse createTaskResponse = createTaskWithPost(title, message);
        Task createdTask = createTaskResponse.getTask();

        updateTaskStateWithRequiredStatus(createdTask.getId(), TaskState.COMPLETED, HttpStatus.OK);
        Task updatedTask = getTaskWithRequiredStatus(createdTask.getId(), HttpStatus.OK).getTask();
        assertThat(updatedTask.getId(), is(createdTask.getId()));
        assertThat(updatedTask.getTitle(), is(createdTask.getTitle()));
        assertThat(updatedTask.getMessage(), is(createdTask.getMessage()));
        assertThat(updatedTask.getTaskState(), is(TaskState.COMPLETED));
    }


    @Test
    public void whenTaskToUpdatedNoExists_thenThrow404Error() throws IOException {
        clearData();
        updateTaskStateWithRequiredStatus(UUID.randomUUID().toString(), TaskState.COMPLETED, HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenTaskDelete_thenThrow404UponGet() throws IOException {
        clearData();
        String title = "This is a Title to Delete task";
        String message = "This is a task message to be deleted";
        TodoList.CreateTaskResponse createTaskResponse = createTaskWithPost(title, message);
        assertThat(createTaskResponse.getTask().getTitle(), is(title));
        deleteTaskWithRequiredStatus(createTaskResponse.getTask().getId(), HttpStatus.OK);
        getTaskWithRequiredStatus(createTaskResponse.getTask().getId(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void whenNoTaskToDelete_thenThrow404() throws IOException {
        clearData();
        deleteTaskWithRequiredStatus(UUID.randomUUID().toString(), HttpStatus.NOT_FOUND);
    }


    private String getSingleTaskUrl(String taskId) {
        return getBaseTaskUrl() + "/" + taskId;
    }

    private String getBaseTaskUrl() {
        return "http://localhost:" + port + "/tasks";
    }

    private List<Task> createTasks(int taskCount) throws IOException {
        List<Task> createdTasks = new Vector<>();
        for (int i = 0; i < taskCount; i++) {
            String title = RandomStringUtils.random(RandomUtils.nextInt(1, 10));
            String message = RandomStringUtils.random(RandomUtils.nextInt(1, 10));
            Task createdTask = createTaskWithPost(title, message).getTask();
            createdTasks.add(createdTask);
        }
        return createdTasks;
    }


    private TodoList.CreateTaskResponse createTaskWithPost(String taskTitle, String taskMessage) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(getBaseTaskUrl());
        TodoList.CreateTaskRequest req = TodoList.CreateTaskRequest.newBuilder().setTitle(taskTitle).setMessage(taskMessage).build();
        String json = JsonFormat.printer().print(req);
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        String responseAsString = new String(response.getEntity().getContent().readAllBytes());
        response.close();
        TodoList.CreateTaskResponse.Builder builder = TodoList.CreateTaskResponse.newBuilder();
        JsonFormat.parser().merge(responseAsString, builder);
        return builder.build();
    }

    private GetTaskResponse getTaskWithRequiredStatus(String taskId, HttpStatus status) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getSingleTaskUrl(taskId));
        HttpResponse httpResponse = httpClient.execute(request);
        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(status.value()));
        if ( status == HttpStatus.OK) {
            byte[] responseAsBytes = httpResponse.getEntity().getContent().readAllBytes();
            String responseAsString = new String(responseAsBytes);
            GetTaskResponse.Builder builder = GetTaskResponse.newBuilder();
            JsonFormat.parser().merge(responseAsString, builder);
            return builder.build();
        } else {
            return GetTaskResponse.newBuilder().build();
        }
    }


    private TodoList.ListTasksResponse listTasksWithRequiredStatus(int offset, int limit, HttpStatus status) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseTaskUrl());
        URI uri = new URIBuilder(request.getURI())
                .addParameter("offset", String.valueOf(offset))
                .addParameter("limit", String.valueOf(limit))
                .build();
        request.setURI(uri);
        HttpResponse httpResponse = httpClient.execute(request);
        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(status.value()));
        if(status == HttpStatus.OK) {
            byte[] responseAsBytes = httpResponse.getEntity().getContent().readAllBytes();
            String responseAsString = new String(responseAsBytes);
            httpClient.close();
            TodoList.ListTasksResponse.Builder builder = TodoList.ListTasksResponse.newBuilder();
            JsonFormat.parser().merge(responseAsString, builder);
            return builder.build();
        } else {
            return TodoList.ListTasksResponse.newBuilder().build();
        }
    }

    private void updateTaskStateWithRequiredStatus(String taskId, TaskState newState, HttpStatus status) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(getSingleTaskUrl(taskId));
        TodoList.UpdateTaskStateRequest request = TodoList.UpdateTaskStateRequest.newBuilder().setId(taskId).setTaskState(newState).build();
        String json = JsonFormat.printer().print(request);
        StringEntity entity = new StringEntity(json);
        httpPut.setEntity(entity);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpPut);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(status.value()));
    }

    private void deleteTaskWithRequiredStatus(String taskId, HttpStatus status) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(getSingleTaskUrl(taskId));
        CloseableHttpResponse response = httpClient.execute(httpDelete);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(status.value()));
        response.close();
    }

    private void clearData() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(getBaseTaskUrl());
        CloseableHttpResponse response = httpClient.execute(httpDelete);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.OK.value()));
        response.close();
    }


    static class TaskByIdComparator implements Comparator<Task> {

        @Override
        public int compare(Task o1, Task o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
}