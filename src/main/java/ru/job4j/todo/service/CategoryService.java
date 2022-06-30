package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.CategoryStore;

import java.util.List;

@Service
public class CategoryService implements GenericService<Category> {

    private final CategoryStore categoryStore;

    public CategoryService(CategoryStore categoryStore) {
        this.categoryStore = categoryStore;
    }

    @Override
    public Category add(Category category) {
        return categoryStore.add(category);
    }

    @Override
    public boolean replace(int id, Category category) {
        return categoryStore.replace(id, category);
    }

    @Override
    public boolean delete(int id) {
        return categoryStore.delete(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryStore.findAll();
    }

    @Override
    public List<Category> findByName(String key) {
        return categoryStore.findByName(key);
    }

    @Override
    public Category findById(int id) {
        return categoryStore.findById(id);
    }
}
