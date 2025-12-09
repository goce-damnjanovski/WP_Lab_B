package mk.ukim.finki.wp.lab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private Chef chef;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    public Rating(Dish dish, int score) {
        this.dish = dish;
        this.score = score;
    }

}
