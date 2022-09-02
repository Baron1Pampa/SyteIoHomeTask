package com.syte.io.semyonm.facade;

import com.syte.io.semyonm.domain.DomainTask;
import com.syte.io.semyonm.domain.TaskState;

import java.util.List;
import java.util.Optional;

public interface ToDoListFacade {

    Optional<DomainTask> getTask(String id);

    Optional<DomainTask> updateTaskState(String taskId, TaskState newTaskState);

    DomainTask createTask(String title, String message);

    List<DomainTask> loadAllTasks(int offset, int limit);

    Optional<DomainTask> deleteTask(String id);
}
