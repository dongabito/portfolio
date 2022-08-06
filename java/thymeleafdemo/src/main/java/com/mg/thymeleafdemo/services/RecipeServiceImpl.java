package com.mg.thymeleafdemo.services;


import com.mg.thymeleafdemo.models.dtos.IngredientDTO;
import com.mg.thymeleafdemo.models.entities.Ingredient;
import com.mg.thymeleafdemo.models.entities.Recipe;
import com.mg.thymeleafdemo.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository repository;
    private IngredientService ingredientService;

    @Override
    public List<Recipe> getAllRecipes() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Recipe getDetails(Long id) {
        return repository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void deleteRecipe(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void addRecipe(Recipe recipe) {
        if (recipe.getId() != null) {
            Map<Ingredient, Double> ingredients = repository.findById(recipe.getId()).get().getIngredients();
            recipe.setIngredients(ingredients);
        }
        repository.save(recipe);
    }

    @Override
    public Recipe getRecipe(Long id) {
        return repository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void addIngredientToRecipe(IngredientDTO dto) {
        Recipe recipeToUpdate = repository.findById(dto.getId()).orElseThrow(IllegalArgumentException::new);
        Ingredient ingredientToAdd = ingredientService.getIngredient(dto.getIngredientId());
        recipeToUpdate.addIngredients(ingredientToAdd, dto.getAmount());
        repository.save(recipeToUpdate);
    }

    @Override
    public void deleteIngredientFromList(Long id, Long idIngr) {
        Recipe recipeToUpdate = repository.findById(id).orElseThrow(IllegalArgumentException::new);
        Ingredient ingredientToRemove = ingredientService.getIngredient(idIngr);
        recipeToUpdate.removeIngredient(ingredientToRemove);
        repository.save(recipeToUpdate);
    }
}
