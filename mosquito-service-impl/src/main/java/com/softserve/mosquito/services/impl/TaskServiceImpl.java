package com.softserve.mosquito.services.impl;


import com.softserve.mosquito.dtos.TaskCreateDto;
import com.softserve.mosquito.dtos.TaskDto;
import com.softserve.mosquito.entities.Estimation;
import com.softserve.mosquito.entities.Task;
import com.softserve.mosquito.repo.api.TaskRepo;
import com.softserve.mosquito.services.api.*;

import com.softserve.mosquito.transformer.EstimationTransformer;
import com.softserve.mosquito.transformer.PriorityTransformer;
import com.softserve.mosquito.transformer.StatusTransformer;
import com.softserve.mosquito.transformer.UserTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.softserve.mosquito.transformer.TaskTransformer.*;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepo taskRepo;
    private SimpMessagingTemplate template;
    private UserService userService;
    private PriorityService priorityService;
    private EstimationService estimationService;
    private StatusService statusService;

    @Autowired
    public TaskServiceImpl(TaskRepo taskRepo, SimpMessagingTemplate template, UserService userService,
                           PriorityService priorityService, EstimationService estimationService,
                           StatusService statusService) {
        this.taskRepo = taskRepo;
        this.template = template;
        this.userService = userService;
        this.priorityService = priorityService;
        this.estimationService = estimationService;
        this.statusService = statusService;
    }

    @Transactional
    @Override
    public TaskDto save(TaskCreateDto taskCreateDto) {
        Task task = transformToEntity(taskCreateDto);
        task = taskRepo.create(task);
        return task == null ? null : toTaskDto(task);
    }

    @Transactional
    @Override
    public TaskDto update(TaskDto taskDto) {
        Task task = taskRepo.update(transformToEntity(taskDto));
        if (task == null)
            return null;
        return toTaskDto(taskRepo.read(task.getId()));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        taskRepo.delete(id);
    }

    @Transactional
    @Override
    public TaskDto getById(Long id) {
        Task task = taskRepo.read(id);
        return toTaskDto(task);
    }

    @Transactional
    @Override
    public TaskDto getParent(Long parentId) {
        return toTaskDto(taskRepo.read(parentId));
    }

    @Transactional
    @Override
    public List<TaskDto> getSubTasks(Long id) {
        return toTaskDtoList(taskRepo.getSubTasks(id));
    }

    @Transactional
    @Override
    public List<TaskDto> getByOwner(Long ownerId) {
        return toTaskDtoList(taskRepo.getByOwner(ownerId));
    }

    @Transactional
    @Override
    public List<TaskDto> getByWorker(Long workerId) {
        return toTaskDtoList(taskRepo.getByWorker(workerId));
    }

    @Transactional
    @Override
    public List<TaskDto> getAllProjects() {
        return toTaskDtoList(taskRepo.getAllProjects());
    }

    @Transactional
    @Override
    public List<TaskDto> getProjectsByOwner(Long ownerId) {
        return toTaskDtoList(taskRepo.getProjectsByOwner(ownerId));
    }

    @Transactional
    @Override
    public List<TaskDto> filterByStatus(List<TaskDto> taskDtoList, Long statusId) {
        List<TaskDto> filteredList = new ArrayList<>();
        for (TaskDto taskDto : taskDtoList) {
            if (taskDto.getStatus().equals(statusId)) {
                filteredList.add(taskDto);
            }
        }
        return filteredList;
    }

    @Transactional
    @Override
    public TaskDto getByTrelloId(String trelloId) {
        return toTaskDto(taskRepo.getByTrelloId(trelloId));
    }

    @Transactional
    @Override
    public boolean isPresent(String trelloId) {
        return (taskRepo.getByTrelloId(trelloId) != null);
    }

    @Transactional
    @Override
    public TaskDto getByName(String name) {
        return toTaskDto(taskRepo.getByName(name));
    }


    @Transactional
    @Override
    public void sendPushMessage(String message, Long userId) {
        template.convertAndSendToUser(String.valueOf(userId), "/queue/reply", message);
    }

    private Task transformToEntity(TaskDto taskDto ) {
        if (taskDto == null) {
            return null;
        } else {
            return Task.builder().
                    id(taskDto.getId())
                    .name(taskDto.getName())
                    .owner(UserTransformer.toEntity(userService.getById(taskDto.getOwnerId())))
                    .worker(UserTransformer.toEntity(userService.getById(taskDto.getWorkerId())))
                    .priority(PriorityTransformer.toEntity(taskDto.getPriority()))
                    .status(StatusTransformer.toEntity(taskDto.getStatus()))
                    .estimation(EstimationTransformer.toEntity(taskDto.getEstimation()))
                    .parentTask(taskDto.getParentId()==null? null : transformToEntity(getParent(taskDto.getParentId())))
                    .trelloId(taskDto.getTrelloId())
                    .build();
        }
    }

    private Task transformToEntity(TaskCreateDto taskCreateDto) {
        if (taskCreateDto == null) return null;
        return Task.builder()
                .id(taskCreateDto.getId())
                .name(taskCreateDto.getName())
                .owner(UserTransformer.toEntity(userService.getById(taskCreateDto.getOwnerId())))
                .worker(UserTransformer.toEntity(userService.getById(taskCreateDto.getWorkerId())))
                .priority(PriorityTransformer.toEntity(priorityService.getById(taskCreateDto.getPriorityId())))
                .status(StatusTransformer.toEntity(statusService.getById(taskCreateDto.getStatusId())))
                .estimation(Estimation.builder().
                timeEstimation(taskCreateDto.getEstimationTime()).
                remaining(taskCreateDto.getEstimationTime()).task(Task.builder()
                .id(taskCreateDto.getId()).build()).build())
                .parentTask(taskCreateDto.getParentId()== null? null : transformToEntity(getById(taskCreateDto.getParentId())))
                .trelloId(taskCreateDto.getTrelloId())
                .build();
    }
}
