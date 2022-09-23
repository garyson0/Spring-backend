package edu.bbte.idde.vgim1978.spring.controller.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class MenuCreationDto {
    @NotEmpty
    private String type;
    @Size(max = 50)
    @NotEmpty
    private String name;
    @Size(max = 300)
    @NotEmpty
    private String description;
    private boolean vegetarian;
    @Positive
    private long calorieWeight;
    @Positive
    private long price;

}
