package mk.ukim.finki.wp.lab.web.controller;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller()
public class ChefDetailsController {

    private final ChefService chefService;
    private final DishService dishService;

    public ChefDetailsController(ChefService chefService, DishService dishService) {
        this.chefService = chefService;
        this.dishService = dishService;
    }

    @GetMapping("/chefDetails")
    public String showChefDetails(@RequestParam Long idOfChef, Model model) {
        Chef chef = chefService.findById(idOfChef);
        if (chef == null) {
            return "redirect:/listChefs";
        }

        model.addAttribute("chef", chef);
        model.addAttribute("nameOfChef", chef.getFirstName() + " " + chef.getLastName());
        model.addAttribute("bioofChef", chef.getBio());
        model.addAttribute("dishes", chef.getDishes());
        model.addAttribute("ratings", chef.getRatings());
        model.addAttribute("idOfChef", chef.getId());

        return "chefDetails"; // Thymeleaf template
    }

    @PostMapping("/chefDetails")
    public String addDishToChef(@RequestParam Long idOfChef,
                                @RequestParam String idOfDish,
                                @RequestParam Integer rating) {

        chefService.addDishToChef(idOfChef, idOfDish);
        chefService.addRatingToDish(idOfChef, idOfDish, rating);

        // Редирект на GET за истата страна
        return "redirect:/chefDetails?idOfChef=" + idOfChef;
    }
}
