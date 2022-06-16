package ru.job4j.todo.persistence;

import ru.job4j.todo.model.Item;

import java.util.List;

public interface Store {

    Item add(Item item);

    boolean replace(int id, Item item);

    boolean delete(int id);

    List<Item> findAll();

    List<Item> findByName(String key);

    Item findById(int id);
}
