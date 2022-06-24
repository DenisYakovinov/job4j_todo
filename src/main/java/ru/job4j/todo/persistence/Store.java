package ru.job4j.todo.persistence;

import java.util.List;

public interface Store<T> {

    T add(T entity);

    boolean replace(int id, T entity);

    boolean delete(int id);

    List<T> findAll();

    List<T> findByName(String key);

    T findById(int id);
}
