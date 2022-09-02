package com.syte.io.semyonm.dao;

import com.syte.io.semyonm.domain.DomainTask;
import com.syte.io.semyonm.domain.DomainTaskByIdComparator;

import java.util.*;

public class InMemoryToDoListDao implements ToDoListDao {

    private final Map<String, DomainTask> dataMap = new HashMap<>();

    @Override
    public Optional<DomainTask> loadTask(String id) {
        return Optional.ofNullable(dataMap.get(id));
    }

    @Override
    public void saveTask(DomainTask task) {
        dataMap.put(task.getId(), task);
    }

    @Override
    public List<DomainTask> loadAllTasks(int offset, int limit) {
        List<DomainTask> allToDoItems = new ArrayList<>(dataMap.values().stream().toList());
        allToDoItems.sort(new DomainTaskByIdComparator());
        if(offset > allToDoItems.size())
            return new ArrayList<>();
        if(offset + limit > allToDoItems.size())
            return allToDoItems.subList(offset, allToDoItems.size());
        return allToDoItems.subList(offset, limit);
    }

    @Override
    public Optional<DomainTask> deleteTask(String id) {
        return Optional.ofNullable(dataMap.remove(id));
    }

    @Override
    public void deleteAll() {
        dataMap.clear();
    }
}


