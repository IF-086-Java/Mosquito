package com.softserve.mosquito.repo.impl;


import com.softserve.mosquito.entities.Comment;
import com.softserve.mosquito.entities.Task;
import com.softserve.mosquito.repo.api.CommentRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepoImpl implements CommentRepo {

    private SessionFactory sessionFactory;

    @Autowired
    public CommentRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Comment create(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.save(comment);
        return comment;
    }

    @Override
    public Comment read(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Comment.class, id);
    }

    @Override
    public Comment update(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.update(comment);
        return comment;
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Comment comment = session.get(Comment.class, id);
        session.delete(comment);
    }

    @Override
    public List<Comment> getByTaskId(Long taskId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT T.comments FROM " + Task.class.getName() + " T WHERE T.id = " + taskId + "")
                .getResultList();
    }
}