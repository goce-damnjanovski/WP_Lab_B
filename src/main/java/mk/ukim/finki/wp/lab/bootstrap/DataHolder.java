package mk.ukim.finki.wp.lab.bootstrap;


import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<Chef> chefs = new ArrayList<>();
    public static List<Dish> dishes = new ArrayList<>();

    @PostConstruct
    public void init(){
        chefs.add(new Chef(0L, "Yotam", "Ottolenghi", "An Israeli-British chef known for his colorful Mediterranean-inspired dishes and bestselling cookbooks.", new ArrayList<>(), new ArrayList<>()));
        chefs.add(new Chef(1L, "Jose", "Andres", "A Spanish-American chef famous for his humanitarian work and innovative tapas-style cooking.", new ArrayList<>(), new ArrayList<>()));
        chefs.add(new Chef(2L, "Gordon", "Ramsay", "A world-renowned British chef known for his fiery personality and Michelin-starred restaurants.", new ArrayList<>(), new ArrayList<>()));
        chefs.add(new Chef(3L, "Massimo", "Bottura", "An Italian chef celebrated for his creative reinterpretations of traditional Italian cuisine.", new ArrayList<>(), new ArrayList<>()));
        chefs.add(new Chef(4L, "Dominique", "Crenn", "A French chef recognized as the first woman in the U.S. to earn three Michelin stars.", new ArrayList<>(), new ArrayList<>()));

        dishes.add(new Dish(Dish.getNextId(),"D_001", "Sushi", "Japanese", 50));
        dishes.add(new Dish(Dish.getNextId(),"D_002", "Ratatouille", "French", 60));
        dishes.add(new Dish(Dish.getNextId(),"D_003", "Tacos al Pastor", "Mexican", 50));
        dishes.add(new Dish(Dish.getNextId(),"D_004", "Lasagna", "Italian", 90));
        dishes.add(new Dish(Dish.getNextId(),"D_005", "Paella", "Spanish", 75));
    }
}
