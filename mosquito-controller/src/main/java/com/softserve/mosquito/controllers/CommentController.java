package com.softserve.mosquito.controllers;

import com.softserve.mosquito.dtos.CommentDto;
import com.softserve.mosquito.services.api.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/{task_id}/")
@Api(value = "Comment controller", description = "Controller for doing CRUD operation with comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(path = "comments")
    @ApiOperation(value = "Add new comment for task", response = CommentDto.class)
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.save(commentDto);
    }

    @GetMapping(path = "/comments")
    @ApiOperation(value = "Get all comments for concrete task", response = CommentDto.class,
            responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getByTaskId(@PathVariable("task_id") Long taskId) {
        if (commentService.getByTaskId(taskId).equals(Collections.emptyList()))
            throw new CommentNotFoundException("Comments for task with Id " + taskId + " not found!");
        return commentService.getByTaskId(taskId);
    }

    @PutMapping(path = "/{comment_id}")
    @ApiOperation(value = "Update wrote comment", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@RequestBody CommentDto comment) {
        return commentService.update(comment);
    }

    @DeleteMapping(path = "/{commentId}")
    @ApiOperation(value = "Delete existing comment", response = HttpStatus.class)
    public HttpStatus deleteComments(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return HttpStatus.OK;
    }
}