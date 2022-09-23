package edu.bbte.idde.vgim1978.spring.controller;

import edu.bbte.idde.vgim1978.spring.controller.dto.incoming.IngredientCreationDto;
import edu.bbte.idde.vgim1978.spring.controller.dto.outgoing.*;
import edu.bbte.idde.vgim1978.spring.controller.exception.NotFoundException;
import edu.bbte.idde.vgim1978.spring.controller.mapper.IngredientMapper;
import edu.bbte.idde.vgim1978.spring.dao.IngredientDao;
import edu.bbte.idde.vgim1978.spring.model.CrudEvent;
import edu.bbte.idde.vgim1978.spring.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/ingredients")
@Slf4j
public class IngredientController {
    @Autowired
    private IngredientDao ingredientDao;

    @Autowired
    private IngredientMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponseDto create(@RequestBody @Valid IngredientCreationDto ingredient) {
        Ingredient ingredientCr = ingredientDao.saveAndFlush(mapper.dtoToIngredient(ingredient));
        return mapper.ingredientToResponseDto(ingredientCr);
    }

    @GetMapping
    public Collection<IngredientResponseDto> findAll() {
        return mapper.ingredientsListToResponseDto(ingredientDao.findAll());
    }

    @GetMapping("/{id}")
    public IngredientResponseDto getById(@PathVariable("id") Long id) {
        try {
            Ingredient ingr = ingredientDao.getById(id);
            return mapper.ingredientToResponseDto(ingr);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }
}
