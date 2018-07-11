package com.softserve.mosquito.transformer;

import com.softserve.mosquito.dtos.*;
import com.softserve.mosquito.entities.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TaskTransformerTest {


    @Test
    public void toTaskDTO() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment Text");
        comment.setId(20L);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Estimation estimation = new Estimation();
        estimation.setId(2L);
        estimation.setTimeEstimation(25);
        estimation.setRemaining(25);
        User user = new User();
        user.setId(2L);
        user.setEmail("test_email");
        user.setPassword("test_password");
        user.setFirstName("test_name");
        user.setLastName("test_surname");
        user.setConfirmed(true);
        Priority priority = new Priority();
        priority.setTitle("middle");
        priority.setId(5L);
        Status status = new Status();
        status.setTitle("TODO");
        status.setId(6L);
        Task task = Task.builder().parentTask(null).childTasks(null).name("Test Task").id(2L).
                comments(comments).estimation(estimation).owner(user).priority(priority).
                status(status).worker(user).build();
        TaskDto taskDto = TaskTransformer.toTaskDto(task);
        assertEquals(task.getId(), taskDto.getId());

    }



    @Test
    public void toTaskDtoList() {
        Comment comment = new Comment();
        comment.setId(4L);
        comment.setText("Test Comment Text");
        comment.setId(20L);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Estimation estimation = new Estimation();
        estimation.setId(2L);
        estimation.setTimeEstimation(25);
        estimation.setRemaining(25);
        User user = new User();
        user.setId(2L);
        user.setEmail("test_email");
        user.setPassword("test_password");
        user.setFirstName("test_name");
        user.setLastName("test_surname");
        user.setConfirmed(true);
        Priority priority = new Priority();
        priority.setTitle("middle");
        priority.setId(2L);
        Status status = new Status();
        status.setTitle("TODO");
        status.setId(3L);
        Task task = Task.builder().parentTask(null).childTasks(null).name("Test Task").id(5L).
                comments(comments).estimation(estimation).owner(user).priority(priority).
                status(status).worker(user).build();
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        List<TaskDto> taskDtos = TaskTransformer.toTaskDtoList(tasks);
        TaskDto taskDto = taskDtos.get(0);
        assertEquals(task.getId(), taskDto.getId());
        assertEquals(task.getName(),taskDto.getName());
    }

    @Test
    public void toTaskDTO_null() {
        Task task= null;
        TaskDto taskDto = TaskTransformer.toTaskDto(task);
        assertNull(taskDto);
    }

}