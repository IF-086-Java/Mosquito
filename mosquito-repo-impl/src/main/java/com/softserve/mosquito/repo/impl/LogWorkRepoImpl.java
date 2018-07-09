package com.softserve.mosquito.repo.impl;

import com.softserve.mosquito.entities.LogWork;
import com.softserve.mosquito.repo.api.LogWorkRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class LogWorkRepoImpl implements LogWorkRepo {

    private static final Logger LOGGER = LogManager.getLogger(LogWorkRepoImpl.class);
    private SessionFactory sessionFactory;

    @Autowired
    public LogWorkRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public LogWorkRepoImpl() {
    }

    @Override
    public LogWork create(LogWork logWork) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Long logworkId = (Long)session.save(logWork);
            logWork.setId(logworkId);
        } catch (HibernateException e) {
            LOGGER.error("Error during save logWork!");
        } finally {
            if (session != null) session.close();
        }
        return logWork;
    }

    @Override
    @Transactional(readOnly = true)
    public LogWork read(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(LogWork.class, id);
    }

    @Override
    @Transactional
    public LogWork update(LogWork logWork) {
        Session session = sessionFactory.getCurrentSession();
        session.update(logWork);
        return logWork;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        LogWork logWork = session.get(LogWork.class, id);
        session.delete(logWork);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogWork> readAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<LogWork> query = session.createQuery("FROM " + LogWork.class.getName(), LogWork.class);
        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogWork> getByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query<LogWork> query = session.createQuery("from " + LogWork.class.getName() +
                " l where l.author.id = :id ", LogWork.class);
        query.setParameter("id", userId);
        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogWork> getByEstimationId(Long estimationId) {
        Session session = sessionFactory.getCurrentSession();
        Query<LogWork> query = session.createQuery("from " + LogWork.class.getName() +
                " l where l.estimation.id = :est ", LogWork.class);
        query.setParameter("est", estimationId);
        return query.list();
    }
}