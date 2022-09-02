package com.syte.io.semyonm.facade;

import com.syte.io.semyonm.domain.DomainTask;
import com.syte.io.semyonm.domain.TaskState;

import java.util.List;
import java.util.Optional;

public class ToDoListFacadeManager implements ToDoListFacade {
    @Override
    public Optional<DomainTask> getTask(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<DomainTask> updateTaskState(String taskId, TaskState newTaskState) {
        return Optional.empty();
    }

    @Override
    public DomainTask createTask(String title, String message) {
        return null;
    }

    @Override
    public List<DomainTask> loadAllTasks(int offset, int limit) {
        return null;
    }

    @Override
    public Optional<DomainTask> deleteTask(String id) {
        return Optional.empty();
    }
}
