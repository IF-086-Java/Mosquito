package com.softserve.mosquito.services.impl;

import com.mongodb.BasicDBObject;
import com.softserve.mosquito.entities.mongo.TaskMongo;
import com.softserve.mosquito.entities.mongo.TasksBoard;
import com.softserve.mosquito.repo.api.TasksBoardRepo;
import com.softserve.mosquito.services.api.TasksBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TasksBoardServiceImpl implements TasksBoardService {

    private TasksBoardRepo tasksBoardRepo;
    private MongoOperations mongoOperations;

    @Autowired
    public TasksBoardServiceImpl(TasksBoardRepo tasksBoardRepo, MongoOperations mongoOperations) {
        this.tasksBoardRepo = tasksBoardRepo;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void add(TaskMongo taskMongo, Long workerId) {
        TasksBoard tasksBoard = tasksBoardRepo.findByWorkerId(workerId);
        if (tasksBoard != null) {
            tasksBoard.getTaskMongos().add(taskMongo);
        } else
            tasksBoard = new TasksBoard(workerId, Arrays.asList(taskMongo));

        mongoOperations.save(tasksBoard);
    }

    @Override
    public void update(TaskMongo taskMongo, Long workerId) {
        Query query = new Query(Criteria.where("taskMongos.taskId").is(taskMongo.getTaskId()));
        mongoOperations.updateFirst(query,
                new Update().set("taskMongos.$.taskName", taskMongo.getTaskName()), TasksBoard.class);
    }

    @Override
    public void delete(Long id) {
        Query query = new Query(
                Criteria.where("taskMongos.taskId").is(id)
        );
        mongoOperations.updateFirst(query, new Update().pull("taskMongos", new BasicDBObject("taskId", id)),
                TasksBoard.class);
    }

    @Override
    public List<TaskMongo> getUserWork(Long userId) {
        TasksBoard tasksBoard = tasksBoardRepo.findByWorkerId(userId);
        return tasksBoard == null ? Collections.emptyList() : tasksBoard.getTaskMongos();
    }

    @Override
    public List<TaskMongo> getByStatusId(Long userId, Long statusId) {
        TasksBoard tasksBoard = tasksBoardRepo.findByWorkerId(userId);

        return tasksBoard == null ? Collections.emptyList() :
                tasksBoard.getTaskMongos().stream().filter(taskMongo -> taskMongo.getStatusId().equals(statusId))
                        .collect(Collectors.toList());
    }
}
