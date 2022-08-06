package com.mg.thymeleafdemo.controllers;

import com.mg.thymeleafdemo.config.WebSecurity;
import com.mg.thymeleafdemo.models.entities.Recipe;
import com.mg.thymeleafdemo.services.IngredientService;
import com.mg.thymeleafdemo.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@WebMvcTest(RecipeController.class)
@Import(WebSecurity.class)
@DisplayName("Test method ")
class RecipeControllerTest {
    @MockBean
    RecipeService service;
    @MockBean
    IngredientService ingredientService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(service.getAllRecipes()).thenReturn(new ArrayList<>(Arrays.asList(Recipe.builder().name("iuiui").id(22L).description("jlkjlk").build())));
    }

    @Test
    @DisplayName("if endpoint return ingredient searched by Id")
    void testIfMethodReturnsIngredient() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    @DisplayName("if endpoint load form")
    @WithMockUser(username = "user")
    void testIfMethodLoadsForm() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("form"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("allergies"));
    }


}