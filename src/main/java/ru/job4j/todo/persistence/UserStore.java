package ru.job4j.todo.persistence;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.exception.PersistenceException;
import ru.job4j.todo.exception.UniqueViolationException;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserStore extends GenericPersistence implements Store<User> {

    public UserStore(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public User add(User user) {
        try {
            return genericPersist(session -> {
                session.save(user);
                return user;
            });
        } catch (ConstraintViolationException e) {
            throw new UniqueViolationException(e.getMessage(), e);
        } catch (HibernateException e) {
            throw new PersistenceException(String.format("User %s can't be added (%s)", user, e.getMessage()), e);
        }
    }

    @Override
    public boolean replace(int id, User user) {
        return genericPersist(session -> {
            Query query = session.createQuery("update User u set u.name = :newName, u.email = :newEmail,"
                    + " u.password = :newPass where u.id = :iId");
            query.setParameter("newName", user.getName());
            query.setParameter("newEmail", user.getEmail());
            query.setParameter("newPass", user.getPassword());
            return query.executeUpdate() > 0;
        });
    }

    @Override
    public boolean delete(int id) {
        return genericPersist(session -> {
            Query query = session.createQuery("delete from User u where u.id = :uId");
            query.setParameter("uId", id);
            return query.executeUpdate() > 0;
        });
    }

    @Override
    public List<User> findAll() {
        return genericPersist(session -> session.createQuery("from User").list());
    }

    @Override
    public List<User> findByName(String key) {
        return genericPersist(session -> {
            Query query = session.createQuery("from User u where u.name = :uName");
            query.setParameter("uName", key);
            return query.list();
        });
    }

    @Override
    public User findById(int id) {
        return genericPersist(session -> {
            Query query = session.createQuery("from User u where u.id = :uId");
            query.setParameter("uId", id);
            return (User) query.uniqueResult();
        });
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return genericPersist(session -> {
            Query query = session.createQuery("from User u where u.email = :uEmail and u.password = :uPassword");
            query.setParameter("uEmail", email);
            query.setParameter("uPassword", password);
            return query.uniqueResultOptional();
        });
    }
}
