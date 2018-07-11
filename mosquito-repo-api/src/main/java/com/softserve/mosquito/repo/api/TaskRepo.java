package com.softserve.mosquito.repo.api;

import com.softserve.mosquito.entities.Task;

import java.util.List;

public interface TaskRepo extends GenericCRUD<Task> {
    List<Task> getSubTasks(Long id);

    List<Task> getAllProjects();
    List<Task> getProjectsByOwner(Long ownerId);

    List<Task> getByOwner(Long ownerId);
    List<Task> getByWorker(Long workerId);

    Task getByName(String name);
    Task readByEst(Long id);
    Task getByTrelloId(String trelloId);
}
