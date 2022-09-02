package com.syte.io.semyonm.domain;

import com.syte.io.semyonm.TodoList;

public class Converters {

    public static TodoList.Task domainToProto(DomainTask domainTask){
        return TodoList.Task
                .newBuilder()
                .setId(domainTask.getId())
                .setTaskState(domainToProto(domainTask.getTaskState()))
                .setTitle(domainTask.getTitle())
                .setMessage(domainTask.getMessage())
                .build();
    }


    public static TodoList.TaskState domainToProto(TaskState taskState) {
        return switch (taskState) {
            case NEW -> TodoList.TaskState.NEW;
            case IN_PROGRESS -> TodoList.TaskState.IN_PROGRESS;
            case COMPLETED -> TodoList.TaskState.COMPLETED;
            case POSTPONED -> TodoList.TaskState.POSTPONED;
            case CANCELLED -> TodoList.TaskState.CANCELLED;
        };
    }

    public static TaskState protoToDomain(TodoList.TaskState taskState) {
        return switch (taskState) {
            case NEW ->  TaskState.NEW;
            case IN_PROGRESS ->TaskState.IN_PROGRESS;
            case COMPLETED -> TaskState.COMPLETED;
            case POSTPONED -> TaskState.POSTPONED;
            case CANCELLED -> TaskState.CANCELLED;
            default -> throw new IllegalArgumentException("Unexpected task state value: " + taskState);
        };
    }

}
