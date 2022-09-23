package edu.bbte.idde.vgim1978.spring.dao.mem;

import edu.bbte.idde.vgim1978.spring.dao.IngredientDao;
import edu.bbte.idde.vgim1978.spring.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
@Profile("mem")
public class IngredientMemDao implements IngredientDao {
    private final Map<Long, Ingredient> ingrDb = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(0L);

    public IngredientMemDao() {
        log.debug("Ingredients preparing.");
        final Ingredient paradicsom = new Ingredient(1L,"paradicsom",50L);
        final Ingredient sajt = new Ingredient(2L,"sajt",70L);
        final Ingredient szalami = new Ingredient(3L,"szalami",80L);

        long id = currentId.getAndIncrement();
        paradicsom.setId(id);
        ingrDb.put(id, paradicsom);

        id = currentId.getAndIncrement();
        sajt.setId(id);
        ingrDb.put(id, sajt);

        id = currentId.getAndIncrement();
        szalami.setId(id);
        ingrDb.put(id, szalami);

    }

    @Override
    public Ingredient saveAndFlush(Ingredient ingredient) {
        log.debug("Add ingredient:" + ingredient);
        long id = currentId.getAndIncrement();
        ingredient.setId(id);
        ingrDb.put(id,ingredient);
        return ingredient;
    }

    public Ingredient update(Long id, Ingredient ingredient) {
        log.info("Updating menu with id: {}", id);
        if (ingrDb.containsKey(id)) {
            ingrDb.put(id, ingredient);
        }
        return ingredient;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Removing ingredient with id: {}",id);
        ingrDb.remove(id);
    }

    @Override
    public Collection<Ingredient> findAll() {
        log.debug("Getting the ingredients " + ingrDb.values());
        return new ArrayList<>(ingrDb.values());
    }

    @Override
    public Ingredient getById(Long id) {
        log.debug("Getting the ingredient by id : {}", id);
        return ingrDb.get(id);
    }

    @Override
    public Collection<Ingredient> findByName(String name) {
        log.debug("Getting ingredient by name: {}",name);
        Collection<Ingredient> resultIngredients = new ArrayList<>();
        for (Ingredient ingredient : ingrDb.values()) {
            if (ingredient.getName().contains(name)) {
                resultIngredients.add(ingredient);
            }
        }
        return resultIngredients;
    }
}
