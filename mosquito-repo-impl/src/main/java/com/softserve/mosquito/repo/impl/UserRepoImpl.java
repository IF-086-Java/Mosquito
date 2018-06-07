package com.softserve.mosquito.repo.impl;

import com.softserve.mosquito.entities.User;
import com.softserve.mosquito.repo.api.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {

    private static final Logger LOGGER = LogManager.getLogger(UserRepoImpl.class);
    private SessionFactory sessionFactory;

    @Autowired
    public UserRepoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User create(User user) {
        try (Session session = sessionFactory.openSession()) {
            Long id = (Long) session.save(user);
            user.setId(id);
        } catch (HibernateException e) {
            LOGGER.error("Error during save user!" + e.getMessage());
        }
        return user;
    }

    @Override
    public User read(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (HibernateException e) {
            LOGGER.error("Reading user was failed!" + e.getMessage());
            return null;
        }
    }

    @Override
    public User update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.update(user);
            session.getTransaction().commit();
            return user;
        } catch (HibernateException e) {
            LOGGER.error("Updating user was failed!" + e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            LOGGER.error("Deleting user was failed!" + e.getMessage());
        }
    }

    @Override
    public List<User> readAll() {
        Query<User> users = sessionFactory.getCurrentSession().createQuery("FROM " + User.class.getName());
        return users.list();
    }
}