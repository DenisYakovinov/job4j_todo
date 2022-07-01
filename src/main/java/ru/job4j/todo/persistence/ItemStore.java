package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ItemStore extends GenericPersistence implements Store<Item> {

    private static final String BASE_SELECT_QUERY_PART = "select distinct i from Item i left join fetch i.categories ";

    public ItemStore(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Item> findAll() {
        return genericPersist(session -> session.createQuery(String.format("%s%s", BASE_SELECT_QUERY_PART, "order by i.created")).list());

    }

    @Override
    public List<Item> findByName(String key) {
        return genericPersist(session -> {
            Query query = session.createQuery(String.format("%s%s", BASE_SELECT_QUERY_PART, "where i.name = :iName"));
            query.setParameter("iName", key);
            return query.list();
        });
    }

    @Override
    public Item findById(int id) {
        return genericPersist(session -> {
            Query query = session.createQuery(String.format("%s%s", BASE_SELECT_QUERY_PART, "where i.id = :iId"));
            query.setParameter("iId", id);
            return (Item) query.uniqueResult();
        });
    }

    public List<Item> findAllDoneItems() {
        return genericPersist(session -> session.createQuery(String.format("%s%s", BASE_SELECT_QUERY_PART, "where i.done = true")).list());
    }

    public List<Item> findAllNew() {
        return genericPersist(session -> {
            LocalDateTime time = LocalDateTime.now().minusDays(1);
            return session.createQuery(String.format("%s%s", BASE_SELECT_QUERY_PART,  "where i.created > :time"))
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

    public Item replaceWithCategories(Item item) {
        return genericPersist(session -> {
            session.update(item);
            return item;
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