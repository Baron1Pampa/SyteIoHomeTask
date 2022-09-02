package com.syte.io.semyonm.spring;

import com.syte.io.semyonm.TodoList.*;
import com.syte.io.semyonm.domain.Converters;
import com.syte.io.semyonm.domain.DomainTask;
import com.syte.io.semyonm.facade.ToDoListFacade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RestController
public class TodoListService {

    private final ToDoListFacade toDoListFacade;

    public TodoListService(ToDoListFacade toDoListFacade) {
        this.toDoListFacade = toDoListFacade;
    }

    @PostMapping(path = "/tasks", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public CreateTaskResponse createTask(@RequestBody CreateTaskRequest request) {
        DomainTask domainTask = toDoListFacade.createTask(request.getTitle(), request.getMessage());
        Task createdTask = Converters.domainToProto(domainTask);
        return CreateTaskResponse.newBuilder().setTask(createdTask).build();
    }


    @GetMapping(path = "/tasks/{id}", produces = {"application/json"})
    @ResponseBody
    public GetTaskResponse toProtoTaskWithErrorOnEmptyResult(@PathVariable(name = "id") String requestId){
        Optional<DomainTask> maybeTask = toDoListFacade.getTask(requestId);
        Task task = toProtoTaskWithErrorOnEmptyResult(requestId, maybeTask);
        return GetTaskResponse.newBuilder().setTask(task).build();
    }

    @GetMapping(path = "/tasks", produces = {"application/json"})
    @ResponseBody
    public ListTasksResponse listTasks(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                        @RequestParam(name = "limit", defaultValue = "10") int limit) {

        List<DomainTask> domainTasks = toDoListFacade.loadAllTasks(offset, limit);
        List<Task> tasks = domainTasks.stream().map(Converters::domainToProto).toList();
        int nextOffset = tasks.size() < limit ? -1 : offset + limit;

        return ListTasksResponse.newBuilder().addAllTasks(tasks).setNextOffset(nextOffset).build();
    }

    @PutMapping(path = "/tasks/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public UpdateTaskStateResponse updateTask(@PathVariable(name = "id") String taskId, @RequestBody UpdateTaskStateRequest request) {
        com.syte.io.semyonm.domain.TaskState domainTaskState;
        try {
            domainTaskState = Converters.protoToDomain(request.getTaskState());
        } catch (IllegalArgumentException e) {
            throw new  ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Illegal task state", e
            );
        }
        Optional<DomainTask> maybeTask = toDoListFacade.updateTaskState(taskId, domainTaskState);
        Task task = toProtoTaskWithErrorOnEmptyResult(taskId, maybeTask);
        return UpdateTaskStateResponse.newBuilder().setTask(task).build();
    }

    @DeleteMapping(path = "/tasks/{id}", produces = {"application/json"})
    public DeleteTaskResponse deleteTask(@PathVariable(name = "id") String taskId) {
        Optional<DomainTask> maybeTask = toDoListFacade.deleteTask(taskId);
        Task task = toProtoTaskWithErrorOnEmptyResult(taskId, maybeTask);
        return DeleteTaskResponse.newBuilder().setTask(task).build();
    }

    private Task toProtoTaskWithErrorOnEmptyResult(String taskId, Optional<DomainTask> maybeTask) {
        DomainTask domainTask = maybeTask.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task with id: " + taskId + "not found"
        ));
        return Converters.domainToProto(domainTask);
    }


}
