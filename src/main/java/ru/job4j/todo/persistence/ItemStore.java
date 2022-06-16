package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ItemStore implements Store {

    private final SessionFactory sessionFactory;

    public ItemStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Item> findAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Item> result = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Item i where i.name = :iName");
        query.setParameter("iName", key);
        List<Item> result = query.list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item findById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Item i where i.id = :iId");
        query.setParameter("iId", id);
        Item item = (Item) query.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public List<Item> findAllDoneItems() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Item> result = session.createQuery("from Item i where i.done = true").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Item> findAllNew() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        List<Item> result = session.createQuery("from Item i where i.created > :time")
                .setParameter("time", time)
                .list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item add(Item item) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("update Item i set i.name = :newName, i.description= :newDescription,"
                + " i.created = :newCreated, i.done = :newDone where i.id = :iId");
        query.setParameter("newName", item.getName());
        query.setParameter("newDescription", item.getDescription());
        query.setParameter("newCreated", item.getCreated());
        query.setParameter("newDone", item.isDone());
        query.setParameter("iId", item.getId());
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return result > 0;
    }

    @Override
    public boolean delete(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("delete from Item i where i.id = :iId");
        query.setParameter("iId", id);
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return result > 0;
    }

    public boolean updateDoneStatus(int id, boolean value) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("update Item i set i.done = :newDone where i.id = :iId");
        query.setParameter("newDone", value);
        query.setParameter("iId", id);
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return result > 0;
    }
}