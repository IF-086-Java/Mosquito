package com.softserve.mosquito.services.api;

import com.softserve.mosquito.dtos.TaskCreateDto;
import com.softserve.mosquito.dtos.TaskDto;

import java.util.List;

public interface TaskService {

    TaskDto save(TaskCreateDto taskCreateDto );

    TaskDto update(TaskCreateDto taskCreateDto);

    void delete(Long id);

    TaskDto getById(Long id);

    TaskDto getParent(Long parentId);

    List<TaskDto> getSubTasks(Long id);

    List<TaskDto> getByOwner(Long ownerId);

    List<TaskDto> getByWorker(Long workerId);

    List<TaskDto> getAllProjects();

    List<TaskDto> getProjectsByOwner(Long ownerId);

    List<TaskDto> filterByStatus(List<TaskDto> taskDtoList, Long statusId);

    boolean isPresent(String name);

    TaskDto getByName(String name);

    TaskDto getSimpleTaskById(Long id);

    TaskDto getSimpleTaskByEstId(Long id);

    TaskDto getByTrelloId(String trelloId);

    void sendPushMessage(String message, Long userId);
}
