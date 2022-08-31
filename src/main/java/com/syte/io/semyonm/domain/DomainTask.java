package com.syte.io.semyonm.domain;

public class DomainTask {
    private String id;
    private TaskState taskState;
    private String title;
    private String message;



    public DomainTask(String id, TaskState taskState, String title, String message) {
        this.id = id;
        this.taskState = taskState;
        this.title = title;
        this.message = message;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
