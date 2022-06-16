package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemService;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/addItem")
    public String toAddItem() {
        return "addItem";
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item) {
         itemService.add(item);
         return "redirect:/index";
    }

    @PostMapping("/updateItem")
    public String updateItem(Model model, @ModelAttribute Item item) {
        itemService.replace(item.getId(), item);
        model.addAttribute("item", item);
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
