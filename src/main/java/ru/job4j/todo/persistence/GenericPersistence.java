package ru.job4j.todo.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.job4j.todo.exception.PersistenceException;

import javax.persistence.RollbackException;
import java.util.function.Function;

public abstract class GenericPersistence {

    private final SessionFactory sessionFactory;

    public GenericPersistence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected <T> T genericPersist(Function<Session, T> command) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (HibernateException e) {
            try {
                session.getTransaction().rollback();
            } catch (RollbackException re) {
                throw new PersistenceException(re.getMessage());
            }
            throw new PersistenceException(e.getMessage());
        } finally {
            session.close();
        }
    }
}
