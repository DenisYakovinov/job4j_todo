package ru.job4j.todo.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.exception.PersistenceException;
import ru.job4j.todo.model.Item;

import javax.persistence.RollbackException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Repository
public class ItemStore implements Store {

    private final SessionFactory sessionFactory;

    public ItemStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T genericPersist(Function<Session, T> command) {
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

    @Override
    public List<Item> findAll() {
        return genericPersist(session -> session.createQuery("from Item").list());
    }

    @Override
    public List<Item> findByName(String key) {
        return genericPersist(session -> {
            Query query = session.createQuery("from Item i where i.name = :iName");
            query.setParameter("iName", key);
            return query.list();
        });
    }

    @Override
    public Item findById(int id) {
        return genericPersist(session -> {
            Query query = session.createQuery("from Item i where i.id = :iId");
            query.setParameter("iId", id);
            return (Item) query.uniqueResult();
        });
    }

    public List<Item> findAllDoneItems() {
        return genericPersist(session -> session.createQuery("from Item i where i.done = true").list());
    }

    public List<Item> findAllNew() {
        return genericPersist(session -> {
            LocalDateTime time = LocalDateTime.now().minusDays(1);
            return session.createQuery("from Item i where i.created > :time")
                    .setParameter("time", time)
                    .list();
        });
    }

    @Override
    public Item add(Item item) {
        return genericPersist(session -> {
            session.save(item);
            return item;
        });
    }

    @Override
    public boolean replace(int id, Item item) {
        return genericPersist(session -> {
            Query query = session.createQuery("update Item i set i.name = :newName, i.description= :newDescription,"
                    + " i.created = :newCreated, i.done = :newDone where i.id = :iId");
            query.setParameter("newName", item.getName());
            query.setParameter("newDescription", item.getDescription());
            query.setParameter("newCreated", item.getCreated());
            query.setParameter("newDone", item.isDone());
            query.setParameter("iId", item.getId());
            return query.executeUpdate() > 0;
        });
    }

    @Override
    public boolean delete(int id) {
        return genericPersist(session -> {
            Query query = session.createQuery("delete from Item i where i.id = :iId");
            query.setParameter("iId", id);
            return query.executeUpdate() > 0;
        });
    }

    public boolean updateDoneStatus(int id, boolean value) {
        return genericPersist(session -> {
            Query query = session.createQuery("update Item i set i.done = :newDone where i.id = :iId");
            query.setParameter("newDone", value);
            query.setParameter("iId", id);
            return query.executeUpdate() > 0;
        });
    }
}