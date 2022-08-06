package com.mg.thymeleafdemo.controllers;

import com.mg.thymeleafdemo.models.dtos.IngredientDTO;
import com.mg.thymeleafdemo.models.entities.Allergy;
import com.mg.thymeleafdemo.models.entities.Recipe;
import com.mg.thymeleafdemo.services.IngredientService;
import com.mg.thymeleafdemo.services.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeController {

    private RecipeService service;
    private IngredientService ingredientService;

    @GetMapping
    public String listRecipes(Model model) {
        model.addAttribute("recipes", service.getAllRecipes());
        return "index";
    }

    @GetMapping("/{id}")
    public String renderDetails(Model model, @PathVariable Long id) {
        model.addAttribute("recipe", service.getDetails(id));
        return "details";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        service.deleteRecipe(id);
        return "redirect:/recipes";
    }

    @GetMapping("/create")
    public String renderCreateForm(Model model) {
        model.addAttribute("allergies", Allergy.values());
        return "form";
    }

    @GetMapping("/update/{id}")
    public String renderCreateForm(Model model, @PathVariable Long id) {
        model.addAttribute("recipe", service.getRecipe(id));
        return "form";
    }

    @PostMapping("/create")
    public String addRecipe(@ModelAttribute Recipe recipe) {
        service.addRecipe(recipe);
        return "redirect:/recipes";
    }

    @GetMapping("/ingredients/{id}")
    public String renderAddIngredientsForm(Model model, @PathVariable Long id) {
        model.addAttribute("recipe", service.getRecipe(id));
        model.addAttribute("ingredients", ingredientService.listIngredients());
        return "ingredients-form";
    }

    @PostMapping("/ingredients")
    public String addIngredients(@ModelAttribute IngredientDTO dto) {
        service.addIngredientToRecipe(dto);
        return "redirect:/recipes/" + dto.getId();
    }

    @GetMapping("/{id}/ingredients/delete/{idIngr}")
    public String deleteIngredientsFromRecipe(@PathVariable Long id, @PathVariable Long idIngr) {
        service.deleteIngredientFromList(id, idIngr);
        return "redirect:/recipes/" + id;
    }
}
