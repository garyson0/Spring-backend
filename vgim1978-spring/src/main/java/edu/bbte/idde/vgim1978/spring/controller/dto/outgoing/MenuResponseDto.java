package edu.bbte.idde.vgim1978.spring.controller.dto.outgoing;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MenuResponseDto implements Serializable {
    @NotNull
    private Long id;
    private String type;
    private String name;
    private String description;
    private boolean vegetarian;
    private long calorieWeight;
    private long price;
}
