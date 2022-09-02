package com.syte.io.semyonm.facade;

import com.syte.io.semyonm.dao.ToDoListDao;
import com.syte.io.semyonm.domain.DomainTask;
import com.syte.io.semyonm.domain.TaskState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ToDoListFacadeManager implements ToDoListFacade {


    private final ToDoListDao dao;

    public ToDoListFacadeManager(ToDoListDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<DomainTask> getTask(String id) {
        return dao.loadTask(id);
    }

    @Override
    public Optional<DomainTask> updateTaskState(String taskId, TaskState newTaskState) {
        List<DomainTask> domainTasks = dao
                .loadTask(taskId)
                .stream()
                .peek(task -> task.setTaskState(newTaskState)).toList();
        return domainTasks.stream().findFirst();
    }

    @Override
    public DomainTask createTask(String title, String message) {
        DomainTask newTask = new DomainTask(
                UUID.randomUUID().toString(),
                TaskState.NEW,
                title,
                message);
        dao.saveTask(newTask);
        return newTask;
    }

    @Override
    public List<DomainTask> loadAllTasks(int offset, int limit) {
        return dao.loadAllTasks(offset, limit);
    }

    @Override
    public Optional<DomainTask> deleteTask(String id) {
        return dao.deleteTask(id);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
