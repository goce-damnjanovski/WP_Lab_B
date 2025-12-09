package mk.ukim.finki.wp.lab.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.repository.DishRepository;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final ChefService chefService;

    public DishServiceImpl(DishRepository dishRepository, ChefService chefService) {
        this.dishRepository = dishRepository;
        this.chefService = chefService;
    }


    @Override
    public List<Dish> listDishes() {
        return dishRepository.findAll();
    }

    @Override
    public Dish findByDishId(String dishId) {
        return dishRepository.findByDishId(dishId);
    }

    @Override
    public Dish findById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    @Override
    public Dish create(String dishId, String name, String cuisine, int preparationTime) {
        Dish dish = new Dish(dishId, name, cuisine, preparationTime);
//        if (dish.getId() == null) {
//            dish.setId(Dish.getNextId());
//        }
        return dishRepository.save(dish);
    }

    @Override
    public Dish update(Long id, String dishId, String name, String cuisine, int preparationTime) {
        Dish existing = this.findById(id);

        if (existing == null)
            return null;

        existing.setDishId(dishId);
        existing.setName(name);
        existing.setCuisine(cuisine);
        existing.setPreparationTime(preparationTime);

        return dishRepository.save(existing);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        List<Chef> chefs = chefService.listChefs();

        for (Chef chef : chefs) {
            chef.getDishes().removeIf(d -> d.getId().equals(id));
            chef.getRatings().removeIf(r -> r.getDish().getId().equals(id));
            chefService.saveChef(chef);
        }
        dishRepository.deleteById(id);
    }


}
