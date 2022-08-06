package com.mg.thymeleafdemo.services;

import com.mg.thymeleafdemo.models.entities.Ingredient;

import java.util.List;

public interface IngredientService {
    Ingredient getIngredient(Long id);

    void addIngredient(Ingredient ingredient);

    void deleteIngredient(Long id);

    List<Ingredient> listIngredients();

}
