package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;

@Repository
public class CategoryStore extends GenericPersistence implements Store<Category> {

    public CategoryStore(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Category add(Category category) {
        return genericPersist(session -> {
            session.save(category);
            return category;
        });
    }

    @Override
    public boolean replace(int id, Category category) {
        return genericPersist(session -> {
            Query query = session.createQuery("update Category c set c.name = :newName where c.id = :cId");
            query.setParameter("newName", category.getName());
            query.setParameter("cId", id);
            return query.executeUpdate() > 0;
        });
    }

    @Override
    public boolean delete(int id) {
        return genericPersist(session -> {
            Query query = session.createQuery("delete from Category c where c.id = :cId");
            query.setParameter("cId", id);
            return query.executeUpdate() > 0;
        });
    }

    @Override
    public List<Category> findAll() {
        return genericPersist(session -> session.createQuery("from Category").list());
    }

    @Override
    public List<Category> findByName(String key) {
        return genericPersist(session -> {
            Query query = session.createQuery("from Category c where c.name = :cName");
            query.setParameter("cName", key);
            return query.list();
        });
    }

    @Override
    public Category findById(int id) {
        return genericPersist(session -> {
            Query query = session.createQuery("from Category c where c.id = :cId");
            query.setParameter("cId", id);
            return (Category) query.uniqueResult();
        });
    }
}
