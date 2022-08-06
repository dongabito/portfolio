package com.mg.thymeleafdemo.models.entities;

import lombok.*;

import javax.persistence.*;

@Entity(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean available;

    @Enumerated(EnumType.STRING)
    private Unit unit;

}
