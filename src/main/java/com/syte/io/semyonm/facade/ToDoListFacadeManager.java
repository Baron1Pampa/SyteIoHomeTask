package com.syte.io.semyonm.facade;

import com.syte.io.semyonm.domain.DomainTask;

import java.util.List;
import java.util.Optional;

public class ToDoListFacadeManager implements ToDoListFacade {
    @Override
    public Optional<DomainTask> getTask(String id) {
        return Optional.empty();
    }

    @Override
    public DomainTask updateTask(DomainTask task) {
        return null;
    }

    @Override
    public DomainTask createTask(DomainTask task) {
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
