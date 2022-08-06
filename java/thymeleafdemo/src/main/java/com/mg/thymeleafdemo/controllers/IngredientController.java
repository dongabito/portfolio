package com.mg.thymeleafdemo.controllers;


import com.mg.thymeleafdemo.models.entities.Ingredient;
import com.mg.thymeleafdemo.models.entities.Unit;
import com.mg.thymeleafdemo.services.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {

    private IngredientService service;

    @GetMapping("/create")
    public String addFormIngredient(Model model) {
        model.addAttribute("units", Unit.values());
        return "ingredient-form";
    }

    @GetMapping("/update/{id}")
    public String updateIngredient(@PathVariable Long id, Model model) {
        model.addAttribute("ingredient", service.getIngredient(id));
        model.addAttribute("units", Unit.values());
        return "ingredient-form";
    }

    @PostMapping("/create")
    public String addIngredient(@ModelAttribute Ingredient ingredient) {
        service.addIngredient(ingredient);
        return "redirect:/recipes";
    }
}
