package com.syte.io.semyonm.spring;

import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.syte.io.semyonm.TodoList;
import com.syte.io.semyonm.TodoList.*;
import com.syte.io.semyonm.facade.ToDoListFacade;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class TodoListService {

    private final ToDoListFacade toDoListFacade;

    public TodoListService(ToDoListFacade toDoListFacade) {
        this.toDoListFacade = toDoListFacade;
    }

    @PostMapping(path = "/tasks", consumes = {"application/x-protobuf", "application/json"}, produces = "application/json")
    @ResponseBody
    public CreateTaskResponse createTask(@RequestBody CreateTaskRequest request) {
        return CreateTaskResponse.newBuilder().build();
    }


    @GetMapping(path = "/tasks/{id}", produces = "application/json")
    @ResponseBody
    public GetTaskResponse getTask(@PathVariable(name = "id") String requestId){
        Task task = Task.newBuilder()
                .setId(requestId)
                .setTaskState(TaskState.IN_PROGRESS)
                .setMessage("Any message")
                .build();


        return GetTaskResponse.newBuilder().setTask(task).build();

    }

    @GetMapping(path = "/tasks", produces = "application/json")
    @ResponseBody
    public ListTasksResponse createTask(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                        @RequestParam(name = "limit", defaultValue = "10") int limit) {
        Task task = Task
                .newBuilder()
                .setId(UUID.randomUUID().toString())
                .setTaskState(TaskState.IN_PROGRESS)
                .setMessage("Some Message")
                .build();

        return ListTasksResponse.newBuilder().addTasks(task).setNextOffset(Int32Value.newBuilder().setValue(offset+1).build()).build();
    }

    @PutMapping(path = "/tasks/{id}", consumes = "application/json", produces = "application/json")
    public UpdateTaskStateResponse createTask(@PathVariable(name = "id") String taskId, @RequestBody UpdateTaskStateRequest request) {
        return UpdateTaskStateResponse.newBuilder().build();
    }

    @DeleteMapping(path = "/tasks/{id}", produces = "application/json")
    public DeleteTaskResponse createTask(@PathVariable(name = "id") String taskId) {
        Task task = Task.newBuilder()
                .setId(taskId)
                .setTaskState(TaskState.IN_PROGRESS)
                .setMessage("Deleted message")
                .build();
        return DeleteTaskResponse.newBuilder()/*.setTask(task)*/.build();
    }





}
