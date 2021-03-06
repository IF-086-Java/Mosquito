package com.softserve.mosquito.services.api;

import com.softserve.mosquito.dtos.TaskSimpleDto;

import java.util.List;

public interface TrelloCardService {

    List<TaskSimpleDto> allNewTrelloTasks(Long userId);

    void createTasksFromTrello(Long userId);

    void createChosenTastsFromTrello(Long userId, List<TaskSimpleDto> taskSimpleDtos);

}
