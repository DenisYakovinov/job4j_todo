package ru.job4j.todo.service;

import ru.job4j.todo.model.Item;

import java.util.List;

public interface GenericService<T> {
    T add(T entity);

    boolean replace(int id, T entity);

    boolean delete(int id);

    List<T> findAll();

    List<T> findByName(String key);

    T findById(int id);
}
