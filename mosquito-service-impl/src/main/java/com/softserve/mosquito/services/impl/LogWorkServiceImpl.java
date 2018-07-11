package com.softserve.mosquito.services.impl;

import com.softserve.mosquito.dtos.LogWorkDto;
import com.softserve.mosquito.entities.Estimation;
import com.softserve.mosquito.entities.LogWork;
import com.softserve.mosquito.entities.Status;
import com.softserve.mosquito.entities.Task;
import com.softserve.mosquito.repo.api.EstimationRepo;
import com.softserve.mosquito.repo.api.LogWorkRepo;
import com.softserve.mosquito.repo.api.StatusRepo;
import com.softserve.mosquito.repo.api.TaskRepo;
import com.softserve.mosquito.services.api.LogWorkService;
import com.softserve.mosquito.transformer.LogWorkTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogWorkServiceImpl implements LogWorkService {

    private LogWorkRepo logWorkRepo;
    private EstimationRepo estimationRepo;
    private TaskRepo taskRepo;
    private StatusRepo statusRepo;



    @Autowired
    public LogWorkServiceImpl(LogWorkRepo logWorkRepo, EstimationRepo estimationRepo, TaskRepo taskRepo, StatusRepo statusRepo) {
        this.logWorkRepo = logWorkRepo;
        this.estimationRepo = estimationRepo;
        this.taskRepo = taskRepo;
        this.statusRepo = statusRepo;
    }

    @Transactional
    @Override
    public LogWorkDto save(Long estId, LogWorkDto logWorkDto, int remaining) {

        LogWork logWork = LogWorkTransformer.toEntity(logWorkDto);
        logWorkDto.setLastUpdate(LocalDateTime.now());

        Estimation estimation = estimationRepo.read(estId);
        estimation.setRemaining(remaining);
        estimationRepo.update(estimation);

      //Task status update
        Task task = estimation.getTask();

        if (logWorkRepo.getByEstimationId(estId)==null){
            Status status = statusRepo.read(2L);
            task.setStatus(status);
            taskRepo.update(task);
        }
        if (remaining==0){
            Status status = statusRepo.read(3L);
            task.setStatus(status);
            taskRepo.update(task);
        }

        logWorkRepo.create(logWork);
        return LogWorkTransformer.toDTO(logWork);
    }

    @Transactional
    @Override
    public LogWorkDto getById(Long logWorkId) {
        return LogWorkTransformer.toDTO(logWorkRepo.read(logWorkId));
    }

    @Transactional
    @Override
    public LogWorkDto update(LogWorkDto logWorkDto, int remaining) {
        LogWork logWork = logWorkRepo.update(LogWorkTransformer.toEntity(logWorkDto));
        Estimation estimation = estimationRepo.read(logWork.getEstimation().getId());
        estimation.setRemaining(remaining);
        estimationRepo.update(estimation);

      //Task status update
        Task task = estimation.getTask();

        if (remaining==0){
            Status status = statusRepo.read(3L);
            task.setStatus(status);
            taskRepo.update(task);
        }
        return LogWorkTransformer.toDTO(logWork);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        logWorkRepo.delete(id);
    }

    @Transactional
    @Override
    public List<LogWorkDto> getByEstimationId(Long estimationId) {
        List<LogWork> logWorks = logWorkRepo.getByEstimationId(estimationId);
        return LogWorkTransformer.toDTOList(logWorks);
    }

    @Transactional
    public List<LogWorkDto> getByUserId(Long userId) {
        List<LogWork> logWorks = logWorkRepo.getByUserId(userId);
        return LogWorkTransformer.toDTOList(logWorks);
    }


}
