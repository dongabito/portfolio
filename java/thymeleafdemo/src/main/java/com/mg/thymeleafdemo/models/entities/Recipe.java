package com.mg.thymeleafdemo.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String steps;

    @ElementCollection
    private Map<Ingredient, Double> ingredients = new HashMap<>();

    @ElementCollection
    private List<Allergy> allergies = new ArrayList<>();

    public void addIngredients(Ingredient ingredient, Double amount) {
        if (this.ingredients == null) this.ingredients = new HashMap<>();
        this.ingredients.put(ingredient, amount);
    }

    public void removeIngredient(Ingredient ingredientToRemove) {
        ingredients.remove(ingredientToRemove);
    }
}
