package mk.ukim.finki.wp.lab.web.controller;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChefController {
    private final ChefService chefService;
    private final DishService dishService;

    public ChefController(ChefService chefService, DishService dishService) {
        this.chefService = chefService;
        this.dishService = dishService;
    }


    @GetMapping("/listChefs")
    public String listChefs(Model model) {
        model.addAttribute("chefs", chefService.listChefs());
        return "listChefs";
    }


    @GetMapping("/dish")
    public String showDishSelectionForm(@RequestParam Long idOfChef, Model model) {
        model.addAttribute("idOfChef", idOfChef);

        Chef chef = chefService.findById(idOfChef);
        if (chef == null) {
            return "redirect:/listChefs";
        }

        model.addAttribute("nameOfChef", chef.getFirstName() + " " + chef.getLastName());
        model.addAttribute("dishes", dishService.listDishes());
        return "dishesList"; // Thymeleaf template за избор на јадења
    }

    @PostMapping("/dish")
    public String handleDish(@RequestParam Long idOfChef,
                             @RequestParam(required = false) String idOfDish,
                             @RequestParam(required = false) Integer rating,
                             Model model) {
        if (idOfDish != null && rating != null) {
            chefService.addDishToChef(idOfChef, idOfDish);
            chefService.addRatingToDish(idOfChef, idOfDish, rating);
            return "redirect:/chefDetails?idOfChef=" + idOfChef;
        }

        model.addAttribute("idOfChef", idOfChef);
        model.addAttribute("nameOfChef", chefService.findById(idOfChef).getFirstName() + " "
                + chefService.findById(idOfChef).getLastName());
        model.addAttribute("dishes", dishService.listDishes());
        return "dishesList";
    }


}
