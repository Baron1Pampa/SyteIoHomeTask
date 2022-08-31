package com.syte.io.semyonm.dao;

import com.syte.io.semyonm.domain.DomainTask;

import java.util.List;
import java.util.Optional;

public interface ToDoListDao {

    Optional<DomainTask> loadTask(String id);

    Optional<DomainTask> saveTask(DomainTask task);

    List<DomainTask> loadAllTasks(int offset, int limit);

    Optional<DomainTask> deleteTask(String id);

}
