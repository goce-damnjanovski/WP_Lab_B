package mk.ukim.finki.wp.lab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chefs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(length = 1000)
    private String bio;

    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Dish> dishes = new ArrayList<>();

    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Rating> ratings = new ArrayList<>();



    public Chef(String firstName, String lastName, String bio, List<Dish> dishes, List<Rating> ratings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.dishes = dishes;
        this.ratings = ratings;
    }


    public int getScoreForDish(Dish dish) {
        for (Rating r : ratings) {
            if (r.getDish().getDishId().equals(dish.getDishId())) {
                return r.getScore();
            }
        }

        throw new RuntimeException("Rating not found for dish: " + dish.getDishId());
    }
}
