package com.iamco.dao;

import com.iamco.exception.DataAccessException;
import com.iamco.exception.ResourceNotFoundException;

import com.iamco.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class GenericDAO<T> {
    private final Class<T> type;

    public GenericDAO(Class<T> type) {
        this.type = type;
    }

    public void save(T entity) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataAccessException("Error saving entity: " + entity, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void update(T entity) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataAccessException("Error updating entity: " + entity, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public T findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(type, id);
        }
    }

    public T getOrThrow(Long id) {
        T entity = findById(id);
        if (entity == null) {
            throw new ResourceNotFoundException(type.getSimpleName() + " with ID " + id + " not found");
        }
        return entity;
    }

    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + type.getName(), type).list();
        }
    }

    public T findByField(String fieldName, Object value) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + type.getName() + " where " + fieldName + " = :value", type)
                    .setParameter("value", value)
                    .uniqueResult();
        }
    }

    public void delete(T entity) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataAccessException("Error deleting entity: " + entity, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
