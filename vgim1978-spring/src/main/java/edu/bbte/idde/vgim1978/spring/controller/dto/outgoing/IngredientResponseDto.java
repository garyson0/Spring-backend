package edu.bbte.idde.vgim1978.spring.controller.dto.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class IngredientResponseDto implements Serializable {

    @NotNull
    private Long id;
    @NotEmpty
    private String name;
    @Positive
    private Long calorieWeight;
}
