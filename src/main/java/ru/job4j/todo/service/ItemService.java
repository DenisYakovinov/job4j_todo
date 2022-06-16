package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.persistence.ItemStore;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService implements GenericService {

    private final ItemStore itemStore;

    public ItemService(ItemStore itemStore) {
        this.itemStore = itemStore;
    }

    @Override
    public List<Item> findAll() {
        return itemStore.findAll();
    }

    @Override
    public List<Item> findByName(String key) {
        return itemStore.findByName(key);
    }

    @Override
    public Item findById(int id) {
        return itemStore.findById(id);
    }

    public List<Item> findAllDoneItems() {
        return itemStore.findAllDoneItems();
    }

    public List<Item> findAllNew() {
        return itemStore.findAllNew();
    }

    @Override
    public Item add(Item item) {
        item.setCreated(LocalDateTime.now());
        return itemStore.add(item);
    }

    @Override
    public boolean replace(int id, Item item) {
        return itemStore.replace(id, item);
    }

    @Override
    public boolean delete(int id) {
        return itemStore.delete(id);
    }

    public boolean updateDoneStatus(int id, boolean value) {
        return itemStore.updateDoneStatus(id, value);
    }
}
