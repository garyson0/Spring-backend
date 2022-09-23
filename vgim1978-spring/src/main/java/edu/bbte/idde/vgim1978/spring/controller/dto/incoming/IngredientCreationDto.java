package edu.bbte.idde.vgim1978.spring.controller.dto.incoming;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class IngredientCreationDto {
    private String name;
    @Positive
    private long calorieWeight;

}
