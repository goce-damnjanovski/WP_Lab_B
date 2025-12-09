package mk.ukim.finki.wp.lab.bootstrap;


import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.repository.ChefRepository;
import mk.ukim.finki.wp.lab.repository.DishRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<Chef> chefs = new ArrayList<>();
    public static List<Dish> dishes = new ArrayList<>();

    private final ChefRepository chefRepository;
    private final DishRepository dishRepository;

    public DataHolder(ChefRepository chefRepository, DishRepository dishRepository) {
        this.chefRepository = chefRepository;
        this.dishRepository = dishRepository;
    }

    //docker-compose down -v
    //docker-compose up -d
    @PostConstruct
    public void init() {
        if (chefRepository.findAll().isEmpty()) {
            chefs.add(new Chef( "Yotam", "Ottolenghi", "An Israeli-British chef known for his colorful Mediterranean-inspired dishes and bestselling cookbooks.", new ArrayList<>(), new ArrayList<>()));
            chefs.add(new Chef( "Jose", "Andres", "A Spanish-American chef famous for his humanitarian work and innovative tapas-style cooking.", new ArrayList<>(), new ArrayList<>()));
            chefs.add(new Chef( "Gordon", "Ramsay", "A world-renowned British chef known for his fiery personality and Michelin-starred restaurants.", new ArrayList<>(), new ArrayList<>()));
            chefs.add(new Chef( "Massimo", "Bottura", "An Italian chef celebrated for his creative reinterpretations of traditional Italian cuisine.", new ArrayList<>(), new ArrayList<>()));
            chefs.add(new Chef( "Dominique", "Crenn", "A French chef recognized as the first woman in the U.S. to earn three Michelin stars.", new ArrayList<>(), new ArrayList<>()));

            chefRepository.saveAll(chefs);

        }
        if (dishRepository.findAll().isEmpty()) {
            dishes.add(new Dish("D_001", "Sushi", "Japanese", 50));
            dishes.add(new Dish("D_002", "Ratatouille", "French", 60));
            dishes.add(new Dish("D_003", "Tacos al Pastor", "Mexican", 50));
            dishes.add(new Dish("D_004", "Lasagna", "Italian", 90));
            dishes.add(new Dish("D_005", "Paella", "Spanish", 75));

            dishRepository.saveAll(dishes);
        }
    }
}
