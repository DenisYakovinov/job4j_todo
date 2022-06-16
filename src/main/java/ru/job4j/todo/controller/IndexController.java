package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemService;

import java.util.List;

@Controller
public class IndexController {

    private final ItemService itemService;

    public IndexController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "index";
    }
}
