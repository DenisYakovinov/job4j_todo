package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.exception.EmailReservedException;
import ru.job4j.todo.exception.PersistenceException;
import ru.job4j.todo.exception.ServiceException;
import ru.job4j.todo.exception.UniqueViolationException;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.UserStore;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements GenericService<User> {

    private final UserStore userStore;

    public UserService(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public User add(User user) {
        try {
            return userStore.add(user);
        } catch (UniqueViolationException e) {
            throw new EmailReservedException(e.getMessage(), e);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean replace(int id, User entity) {
        return userStore.replace(id, entity);
    }

    @Override
    public boolean delete(int id) {
        return userStore.delete(id);
    }

    @Override
    public List<User> findAll() {
        return userStore.findAll();
    }

    @Override
    public List<User> findByName(String key) {
        return userStore.findByName(key);
    }

    @Override
    public User findById(int id) {
        return userStore.findById(id);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return userStore.findUserByEmailAndPwd(email, password);
    }
}
