package com.softserve.mosquito.repo.impl;

import com.softserve.mosquito.entities.Task;
import com.softserve.mosquito.repo.api.TaskRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Repository
public class TaskRepoImpl implements TaskRepo {
    private static final Logger LOGGER = LogManager.getLogger(TaskRepoImpl.class);
    private SessionFactory sessionFactory;

    @Autowired
    public TaskRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Task create(Task task) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Long taskId = (Long)session.save(task);
            task.setId(taskId);
            return task;
        } catch (HibernateException e) {
            LOGGER.error("Problem with creating task" + Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Task read(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Task.class, id);
    }

    @Override
    @Transactional
    public Task update(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.update(task);
        return task;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Task task = session.get(Task.class, id);
        session.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getSubTasks(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery("select t FROM " + Task.class.getName() +
                " t JOIN t.parentTask p WHERE p.id = :parentId ", Task.class);
        query.setParameter("parentId", id);
        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllProjects() {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery("FROM " + Task.class.getName() +
                " WHERE parentTask = null ", Task.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getProjectsByOwner(Long ownerId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery(
                "FROM " + Task.class.getName() + " t WHERE t.parentTask = null " +
                        "AND t.owner.id = :ownerId", Task.class);
        query.setParameter("ownerId", ownerId);
        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getByOwner(Long ownerId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery("FROM " + Task.class.getName() +
                " t WHERE t.owner.id= :ownerId ", Task.class);
        query.setParameter("ownerId", ownerId);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getByWorker(Long workerId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery("FROM " + Task.class.getName() +
                " t WHERE t.worker.id = :workerId ", Task.class);
        query.setParameter("workerId", workerId);
        return query.list();
    }

    /*
      methods required by trello
     */
    @Override
    @Transactional(readOnly = true)
    public Task getByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery("FROM " + Task.class.getName() +
                " WHERE name = :taskName ", Task.class);
        query.setParameter("taskName", name);
        return query.uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Task getByTrelloId(String trelloId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery("FROM " + Task.class.getName() +
                " WHERE trello_id = :trelloId ", Task.class);
        query.setParameter("trelloId", trelloId);
        return query.uniqueResult();
    }
}
