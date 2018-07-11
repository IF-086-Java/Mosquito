package com.softserve.mosquito.transformer;

import com.softserve.mosquito.dtos.*;
import com.softserve.mosquito.entities.*;

import java.util.ArrayList;
import java.util.List;

public class TaskTransformer {

    private TaskTransformer() {
        throw new IllegalStateException("Utility class");
    }

    public static Task toEntity(TaskCreateDto taskCreateDto) {
        if (taskCreateDto == null) {
            return null;
        } else {
            return Task.builder().
                    id(taskCreateDto.getId())
                    .name(taskCreateDto.getName())
                    .owner(User.builder().id(taskCreateDto.getOwnerId()).build())
                    .worker(User.builder().id(taskCreateDto.getWorkerId()).build())
                    .priority(Priority.builder().id(taskCreateDto.getPriorityId()).build())
                    .status(Status.builder().id(1L).build())
                    .estimation(Estimation.builder().
                            timeEstimation(taskCreateDto.getEstimationTime()).
                            remaining(taskCreateDto.getEstimationTime()).build())
                    .parentTask(Task.builder().id((taskCreateDto.getParentId() == null ? null : taskCreateDto.getParentId())).build())
                    .trelloId(taskCreateDto.getTrelloId())
                    .build();
        }
    }


    public static TaskDto toTaskDto(Task task) {
        if(task== null){
            return null;
        }
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .ownerId(task.getOwner().getId())
                .workerId(task.getWorker().getId())
                .estimation(EstimationTransformer.toDTO(task.getEstimation()))
                .status(StatusTransformer.toDTO(task.getStatus()))
                .priority(PriorityTransformer.toDTO(task.getPriority()))
                .parentId((task.getParentTask() == null ? null : task.getParentTask().getId()))
                .build();

    }

    public static List<TaskDto> toTaskDtoList(List<Task> tasks) {
        List<TaskDto> taskDtos = new ArrayList<>();
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
                taskDtos.add(toTaskDto(task));
            }
        }
        return taskDtos;
    }

    public static TaskCreateDto toTaskCreateDto(TaskDto taskDto){
    return TaskCreateDto.builder().id(taskDto.getId())
            .name(taskDto.getName())
            .ownerId(taskDto.getOwnerId())
            .workerId(taskDto.getWorkerId())
            .estimationTime(taskDto.getEstimation().getTimeEstimation())
            .statusId(taskDto.getStatus().getId())
            .priorityId(taskDto.getPriority().getId())
            .parentId((taskDto.getParentId())).build();}
}
