package com.mg.thymeleafdemo.repositories;

import com.mg.thymeleafdemo.models.entities.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
