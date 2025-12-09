package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.bootstrap.DataHolder;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.model.Rating;
import mk.ukim.finki.wp.lab.repository.ChefRepository;
import mk.ukim.finki.wp.lab.repository.DishRepository;
import mk.ukim.finki.wp.lab.service.ChefService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChefServiceImpl implements ChefService {
    private final ChefRepository chefRepository;
    private final DishRepository dishRepository;


    public ChefServiceImpl(ChefRepository chefRepository, DishRepository dishRepository) {
        this.chefRepository = chefRepository;
        this.dishRepository = dishRepository;
    }


    @Override
    public List<Chef> listChefs() {
        return chefRepository.findAll();
    }

    @Override
    public Chef findById(Long id) {
        return chefRepository.findById(id).orElse(null);
    }

    @Override
    public Chef addDishToChef(Long chefId, String dishId) {
        Chef chef = chefRepository.findById(chefId).orElse(null);
        Dish dish = dishRepository.findByDishId(dishId);

        if (chef == null || dish == null) return null;

        chef.getDishes().add(dish);
        dish.setChef(chef);
        return chefRepository.save(chef);
    }

    @Override
    public Chef addRatingToDish(Long chefId, String dishId, int score) {
        Chef chef = chefRepository.findById(chefId).orElseThrow();
        Dish dish = dishRepository.findByDishId(dishId);

        Rating rating = new Rating();
        rating.setDish(dish);
        rating.setChef(chef);
        rating.setScore(score);

        chef.getRatings().add(rating);

        return chefRepository.save(chef);
    }

    @Override
    public Chef saveChef(Chef chef) {
        return chefRepository.save(chef);
    }
}
