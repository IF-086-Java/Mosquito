package com.softserve.mosquito.services.impl;

import com.softserve.mosquito.dtos.CommentDto;
import com.softserve.mosquito.entities.Comment;
import com.softserve.mosquito.repo.api.CommentRepo;
import com.softserve.mosquito.services.api.CommentService;
import com.softserve.mosquito.transformer.CommentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepo repo;

    @Autowired
    public CommentServiceImpl(CommentRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public CommentDto getById(Long id) {
        Comment comment = repo.read(id);
        return comment == null ? null : CommentTransformer.toDTO(comment);
    }

    @Override
    @Transactional
    public CommentDto save(CommentDto commentDto) {
        Comment comment = repo.create(CommentTransformer.toEntity(commentDto));
        return comment == null ? null : CommentTransformer.toDTO(comment);
    }

    @Override
    @Transactional
    public CommentDto update(CommentDto commentDto) {
        Comment comment = repo.update(CommentTransformer.toEntity(commentDto));
        return comment == null ? null : CommentTransformer.toDTO(comment);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    @Transactional
    public List<CommentDto> getByTaskId(Long taskId) {
        List<Comment> comments = repo.getByTaskId(taskId);
        return CommentTransformer.toDTOList(comments);
    }
}
