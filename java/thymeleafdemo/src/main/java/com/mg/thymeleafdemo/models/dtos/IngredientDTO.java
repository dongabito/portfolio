package com.mg.thymeleafdemo.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {

    private Long id;

    private Long ingredientId;

    private Double amount;
}
