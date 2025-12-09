package mk.ukim.finki.wp.lab.web.controller;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DishController {

    private final DishService dishService;
    private final ChefService chefService;

    public DishController(DishService dishService, ChefService chefService) {
        this.dishService = dishService;
        this.chefService = chefService;
    }

    // Администраторска страна – листа на јадења
    @GetMapping("/dishes")
    public String getDishesPage(@RequestParam(required = false) String error, Model model) {
        List<Dish> dishes = dishService.listDishes();
        model.addAttribute("dishes", dishes);
        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "dishesList"; // Thymeleaf template
    }

    // Форма за додавање ново јадење
    @GetMapping("/dishes/dish-form")
    public String getAddDishPage(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("editMode", false);
        return "dish-form";
    }

    // Форма за уредување постоечко јадење
    @GetMapping("/dishes/dish-form/{id}")
    public String getEditDishForm(@PathVariable Long id, Model model) {
        Dish dish = dishService.findById(id);
        if (dish == null) {
            return "redirect:/dishes?error=DishNotFound";
        }
        model.addAttribute("dish", dish);
        model.addAttribute("editMode", true);
        return "dish-form";
    }

    // POST – додавање ново јадење
    @PostMapping("/dishes/add")
    public String saveDish(@RequestParam String dishId,
                           @RequestParam String name,
                           @RequestParam String cuisine,
                           @RequestParam int preparationTime) {
        dishService.create(dishId, name, cuisine, preparationTime);
        return "redirect:/dishes";
    }

    // POST – уредување постоечко јадење
    @PostMapping("/dishes/edit/{id}")
    public String editDish(@PathVariable Long id,
                           @RequestParam String dishId,
                           @RequestParam String name,
                           @RequestParam String cuisine,
                           @RequestParam int preparationTime) {
        dishService.update(id, dishId, name, cuisine, preparationTime);
        return "redirect:/dishes";
    }

    // POST – бришење јадење
    @PostMapping("/dishes/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        dishService.delete(id);
        return "redirect:/dishes";
    }


}
