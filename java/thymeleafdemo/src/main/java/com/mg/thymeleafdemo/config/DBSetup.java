package com.mg.thymeleafdemo.config;

import com.mg.thymeleafdemo.models.entities.Allergy;
import com.mg.thymeleafdemo.models.entities.Ingredient;
import com.mg.thymeleafdemo.models.entities.Recipe;
import com.mg.thymeleafdemo.models.entities.Unit;
import com.mg.thymeleafdemo.repositories.IngredientRepository;
import com.mg.thymeleafdemo.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class DBSetup implements ApplicationListener<ContextRefreshedEvent> {

    private RecipeRepository repository;
    private IngredientRepository ingredientRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Ingredient ingredient1 = Ingredient.builder().name("só").unit(Unit.KÁVÉSKANÁL).available(true).build();
        Ingredient ingredient2 = Ingredient.builder().name("comb").unit(Unit.KILOGRAMM).available(true).build();
        Ingredient ingredient3 = Ingredient.builder().name("tejföl").unit(Unit.KÁVÉSKANÁL).available(false).build();
        ingredientRepository.save(ingredient1);
        ingredientRepository.save(ingredient2);
        ingredientRepository.save(ingredient3);
        Recipe recipe1 = Recipe.builder().description("Gulyás alföldi módra").name("Gulyás").allergies(Arrays.asList(Allergy.FEHÉRJE, Allergy.GLUTÉN)).build();
        recipe1.addIngredients(ingredient1, 1.0);
        recipe1.addIngredients(ingredient2, 3.0);
        recipe1.addIngredients(ingredient3, 2.0);

        repository.save(recipe1);
    }
}
