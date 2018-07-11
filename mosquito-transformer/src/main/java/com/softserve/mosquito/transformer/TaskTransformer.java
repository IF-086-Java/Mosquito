package com.softserve.mosquito.transformer;

import com.softserve.mosquito.dtos.*;
import com.softserve.mosquito.entities.*;

import java.util.ArrayList;
import java.util.List;



public class TaskTransformer {

    private TaskTransformer() {
        throw new IllegalStateException("Utility class");
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
                .trelloId(task.getTrelloId())
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
}
