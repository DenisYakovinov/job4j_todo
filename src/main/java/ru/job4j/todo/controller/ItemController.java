package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.ItemService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    public ItemController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping("/addItem")
    public String toAddItem(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "addItem";
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item,
                             @RequestParam("categoriesIds") List<Integer> categoriesIds,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        item.setUser(user);
        itemService.add(item);
        return "redirect:/index";
    }

    @PostMapping("/updateItem")
    public String updateItem(Model model, @ModelAttribute Item item) {
        itemService.replace(item.getId(), item);
        model.addAttribute("item", itemService.findById(item.getId()));
        return "itemDetail";
    }

    @GetMapping("/completedItems")
    public String completedItems(Model model) {
        model.addAttribute("items", itemService.findAllDoneItems());
        return "index";
    }

    @GetMapping("/newItems")
    public String newItems(Model model) {
        model.addAttribute("items", itemService.findAllNew());
        return "index";
    }

    @GetMapping("/detail/{itemId}")
    public String itemDetail(Model model, @PathVariable("itemId") int itemId) {
        model.addAttribute("item", itemService.findById(itemId));
        return "itemDetail";
    }

    @GetMapping("/completed/{itemId}")
    public String completeItem(Model model, @PathVariable("itemId") int itemId) {
        itemService.updateDoneStatus(itemId, true);
        model.addAttribute("item", itemService.findById(itemId));
        return "itemDetail";
    }

    @GetMapping("/removeItem/{itemId}")
    public String removeItem(Model model, @PathVariable("itemId") int itemId) {
        itemService.delete(itemId);
        return "redirect:/index";
    }

    @GetMapping("/editItem/{itemId}")
    public String editItem(Model model, @PathVariable("itemId") int itemId) {
        model.addAttribute("item", itemService.findById(itemId));
        return "editItem";
    }
}
