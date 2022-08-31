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
        switch (taskState){
            case NEW:
                return TodoList.TaskState.NEW;
            case IN_PROGRESS:
                return TodoList.TaskState.IN_PROGRESS;
            case COMPLETED:
                return TodoList.TaskState.COMPLETED;
            case POSTPONED:
                return TodoList.TaskState.POSTPONED;
            case CANCELLED:
                return TodoList.TaskState.CANCELLED;
        }
        return TodoList.TaskState.UNKNOWN;
    }

}
