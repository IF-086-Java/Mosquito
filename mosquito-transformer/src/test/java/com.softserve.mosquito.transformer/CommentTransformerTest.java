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

public class CommentTransformerTest {

    @Test
    public void toEntity() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Test Comment");
        EstimationDto estimationDto = new EstimationDto();
        estimationDto.setId(3L);
        estimationDto.setTimeEstimation(25);
        estimationDto.setRemaining(25);
        SpecializationDto specializationDto1 = new SpecializationDto();
        specializationDto1.setId(2L);
        specializationDto1.setTitle("QC");
        SpecializationDto specializationDto2 = new SpecializationDto();
        specializationDto2.setId(5L);
        specializationDto2.setTitle("QA");
        Set<SpecializationDto> specializationDtos = new HashSet<>();
        specializationDtos.add(specializationDto1);
        specializationDtos.add(specializationDto2);
        UserDto userDto = UserDto.builder().email("test_email").password("test_passpword").id(4L).
                firstName("test_name").lastName("test_surname").
                specializations(specializationDtos).build();
        PriorityDto priorityDto = new PriorityDto();
        priorityDto.setId(1L);
        priorityDto.setTitle("High");
        StatusDto statusDto = new StatusDto();
        statusDto.setId(2L);
        statusDto.setTitle("Doing");
        TaskCreateDto taskCreateDto = new TaskCreateDto();
        taskCreateDto.setEstimationTime(100);
        taskCreateDto.setOwnerId(5L);
        taskCreateDto.setPriorityId(1L);
        taskCreateDto.setStatusId(2L);
        taskCreateDto.setName("Test Task");
        taskCreateDto.setId(10L);
        commentDto.setAuthorId(userDto.getId());
        commentDto.setTaskId(taskCreateDto.getId());
        Comment comment = CommentTransformer.toEntity(commentDto);
        assertEquals(commentDto.getId(), comment.getId());
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(commentDto.getAuthorId(), comment.getAuthor().getId());
    }

    @Test
    public void toDTO() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment Text");
        Estimation estimation = new Estimation();
        estimation.setId(2L);
        estimation.setTimeEstimation(25);
        estimation.setRemaining(25);
        User user = new User();
        user.setId(3L);
        user.setEmail("test_email");
        user.setPassword("test_password");
        user.setFirstName("test_name");
        user.setLastName("test_surname");
        user.setConfirmed(true);
        Priority priority = new Priority(2L,"middle");
        priority.setId(2L);
        Status status = new Status(1L,"TODO");
        status.setId(3L);
        Task task = Task.builder().name("Test task").estimation(estimation).id(4L)
                .owner(user).worker(user).priority(priority).status(status).build();
        comment.setAuthor(user);
        comment.setTask(task);
        CommentDto commentDto = CommentTransformer.toDTO(comment);
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getAuthor().getId(), commentDto.getAuthorId());
    }

    @Test
   public void toDTOList() {
        Comment comment1 = new Comment();
        comment1.setId(2L);
        comment1.setText("Test Comment Text");
        Estimation estimation = new Estimation();
        estimation.setId(3L);
        estimation.setTimeEstimation(25);
        estimation.setRemaining(25);
        User user = new User();
        user.setId(3L);
        user.setEmail("test_email");
        user.setPassword("test_password");
        user.setFirstName("test_name");
        user.setLastName("test_surname");
        user.setConfirmed(true);
        Priority priority = new Priority(2L,"middle");
        priority.setId(1L);
        Status status = new Status(1L,"TODO");
        status.setId(3L);
        Task task = Task.builder().name("Test task").estimation(estimation).id(8L)
                .owner(user).worker(user).priority(priority).status(status).build();
        comment1.setAuthor(user);
        comment1.setTask(task);

        Comment comment2 = new Comment();
        comment2.setText("New Test Comment Text");
        Estimation estimation1 = new Estimation();
        estimation1.setId(5L);
        estimation1.setTimeEstimation(25);
        estimation1.setRemaining(25);
        User user1 = new User();
        user1.setId(4L);
        user1.setEmail("test_email");
        user1.setPassword("test_password");
        user1.setFirstName("test_name");
        user1.setLastName("test_surname");
        user1.setConfirmed(true);
        Priority priority1 = new Priority(2L,"middle");
        priority1.setId(10L);
        Status status1 = new Status(1L,"TODO");
        status1.setId(2L);
        Task task1 = Task.builder().name("Test task two").estimation(estimation1).id(6L)
                .owner(user1).worker(user1).priority(priority1).status(status1).build();
        comment2.setAuthor(user1);
        comment2.setTask(task1);

        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        List<CommentDto> commentDtos = CommentTransformer.toDTOList(comments);
        CommentDto firstDto = commentDtos.stream().findFirst().get();
        assertEquals(comment1.getText(), firstDto.getText());
    }
     @Test
     public void toEntity_null() {
        CommentDto commentDto = null;
        Comment comment = CommentTransformer.toEntity(commentDto);
        assertNull(comment);
     }
    @Test
    public void toDTO_null() {
        Comment comment = null;
        CommentDto commentDto = CommentTransformer.toDTO(comment);
        assertNull(commentDto);
    }
}