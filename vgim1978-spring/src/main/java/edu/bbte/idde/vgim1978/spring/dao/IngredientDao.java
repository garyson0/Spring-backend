package edu.bbte.idde.vgim1978.spring.dao;

import edu.bbte.idde.vgim1978.spring.model.Ingredient;

import java.util.Collection;

public interface IngredientDao extends Dao<Ingredient> {
    Collection<Ingredient> findByName(String name);

}
