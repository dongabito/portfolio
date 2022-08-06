package com.mg.thymeleafdemo.services;

import com.mg.thymeleafdemo.models.entities.Ingredient;
import com.mg.thymeleafdemo.repositories.IngredientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private IngredientRepository repository;

    @Override
    public Ingredient getIngredient(Long id) {
        return repository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        repository.save(ingredient);
    }

    @Override
    public void deleteIngredient(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Ingredient> listIngredients() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
