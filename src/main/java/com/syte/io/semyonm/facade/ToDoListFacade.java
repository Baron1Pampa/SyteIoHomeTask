package com.syte.io.semyonm.facade;

import com.syte.io.semyonm.domain.DomainTask;

import java.util.List;
import java.util.Optional;

public interface ToDoListFacade {

    Optional<DomainTask> getTask(String id);

    DomainTask updateTask(DomainTask task);

    DomainTask createTask(DomainTask task);

    List<DomainTask> loadAllTasks(int offset, int limit);

    Optional<DomainTask> deleteTask(String id);
}
