package edu.bbte.idde.vgim1978.spring.controller.mapper;

import edu.bbte.idde.vgim1978.spring.controller.dto.incoming.IngredientCreationDto;
import edu.bbte.idde.vgim1978.spring.controller.dto.outgoing.*;
import edu.bbte.idde.vgim1978.spring.model.Ingredient;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient dtoToIngredient(IngredientCreationDto ingredientCreationDto);

    IngredientResponseDto ingredientToResponseDto(Ingredient ingr);

    @IterableMapping(elementTargetType = IngredientResponseDto.class)
    Collection<IngredientResponseDto> ingredientsListToResponseDto(Collection<Ingredient> ingredients);

}
