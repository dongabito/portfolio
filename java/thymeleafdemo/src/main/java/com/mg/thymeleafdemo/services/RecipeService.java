package com.mg.thymeleafdemo.services;

import com.mg.thymeleafdemo.models.dtos.IngredientDTO;
import com.mg.thymeleafdemo.models.entities.Recipe;

import java.util.List;

public interface RecipeService {

    List<Recipe> getAllRecipes();

    Recipe getDetails(Long id);

    void deleteRecipe(Long id);

    void addRecipe(Recipe recipe);

    Recipe getRecipe(Long id);

    void addIngredientToRecipe(IngredientDTO dto);

    void deleteIngredientFromList(Long id, Long idIngr);
}
