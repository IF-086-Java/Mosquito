package com.softserve.mosquito.controllers;

import com.softserve.mosquito.dtos.TaskCreateDto;
import com.softserve.mosquito.dtos.TaskDto;
import com.softserve.mosquito.entities.mongo.TaskMongo;
import com.softserve.mosquito.services.api.TaskService;
import com.softserve.mosquito.services.api.TasksBoardService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tasks")
@Api(value = "Task controller", description = "Controller for doing CRUD operation with task")
public class TaskController {

    private TaskService taskService;
    private TasksBoardService tasksBoardService;

    @Autowired
    public TaskController(TaskService taskService, TasksBoardService tasksBoardService) {
        this.taskService = taskService;
        this.tasksBoardService = tasksBoardService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody TaskCreateDto taskCreateDto) {
        TaskDto taskDto = taskService.save(taskCreateDto);
        tasksBoardService.add(new TaskMongo(taskDto.getId(), taskDto.getName(), taskDto.getStatus().getId()),
                taskDto.getWorkerId());
        taskService.sendPushMessage("<h3>You were assigned to the task! <br>Click here for extra information</h3>", taskDto.getWorkerId());
        return taskDto;
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
        tasksBoardService.update(new TaskMongo(taskDto.getId(), taskDto.getName(), taskDto.getPriority().getId()),
                taskDto.getWorkerId());
        return taskService.update(taskDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteTask(@PathVariable("id") Long id) {
        taskService.delete(id);
        tasksBoardService.delete(id);
        if (taskService.getById(id) == null)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().build();

    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto getTaskById(@PathVariable("id") Long id) {
        if (taskService.getById(id) == null)
            throw new TaskNotFoundException("Task with Id " + id + " not found!");
        return taskService.getById(id);
    }

    @GetMapping(path = "{id}/sub-tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getSubTasks(@PathVariable("id") Long id) {
        return taskService.getSubTasks(id);
    }

    @GetMapping(path = "trello-task/{trello_id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isPresent(@PathVariable("trello_id") String trelloId) {
        return taskService.isPresent(trelloId);
    }

    @GetMapping(path = "/workers-tasks/{worker_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskMongo> getWorkerTasks(@PathVariable("worker_id") Long workerId,
                                          @RequestParam(value = "status_id", required = false) Long statusId) {
        if (statusId != null)
            return tasksBoardService.getByStatusId(workerId, statusId);
        return tasksBoardService.getUserWork(workerId);
    }
}