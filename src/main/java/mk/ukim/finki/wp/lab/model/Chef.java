package mk.ukim.finki.wp.lab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chef {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private List<Dish> dishes =  new ArrayList<>();
    private List<Rating> ratings = new ArrayList<>();

    public void addRating(Dish dish, int rating) {
        this.ratings.add(new Rating(dish, rating));
    }

}
