package edu.bbte.idde.vgim1978.spring.dao.springdata;

import edu.bbte.idde.vgim1978.spring.dao.IngredientDao;
import edu.bbte.idde.vgim1978.spring.model.Ingredient;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("data")
@Repository
public interface IngredientRepository extends IngredientDao, JpaRepository<Ingredient,Long> {
    //Collection<Ingredient> findByMaxCalorieWeight();
}
