package edu.bbte.idde.vgim1978.spring.controller;

import edu.bbte.idde.vgim1978.spring.controller.dto.incoming.MenuCreationDto;
import edu.bbte.idde.vgim1978.spring.controller.dto.outgoing.MenuResponseDto;
import edu.bbte.idde.vgim1978.spring.controller.exception.NotFoundException;
import edu.bbte.idde.vgim1978.spring.controller.mapper.MenuMapper;
import edu.bbte.idde.vgim1978.spring.dao.IngredientDao;
import edu.bbte.idde.vgim1978.spring.model.Ingredient;
import edu.bbte.idde.vgim1978.spring.model.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@RestController
@RequestMapping("/ingredients/{id}/menus")
@Slf4j
public class IngredientsMenuController {
    // main ingredientként funkcionálnak az ingredientek
    // azaz 1 ingredient --> több menu

    @Autowired
    private IngredientDao ingredientDao;

    @Autowired
    private MenuMapper menuMapper;

    @GetMapping
    public Collection<MenuResponseDto> getMenusByIngredient(@PathVariable("id") Long ingredientId) {
        try {
            Ingredient ingr = ingredientDao.getById(ingredientId);
            Collection<Menu> menusOfIngr = ingr.getMenus();
            return menuMapper.menuListToResponseDtoList(menusOfIngr);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @PostMapping
    public MenuResponseDto addMenuToIngredient(@PathVariable("id") Long ingredientId,
                                               @RequestBody MenuCreationDto menu) {
        try {
            Ingredient ingr = ingredientDao.getById(ingredientId);
            Menu returnMenu = new Menu();
            Menu forSearch = menuMapper.dtoToMenu(menu);
            forSearch.setMainIngredient(ingr);
            Collection<Menu> menus = ingr.getMenus();
            menus.add(forSearch);
            ingr.setMenus(menus);
            ingredientDao.saveAndFlush(ingr);

            for (Menu i : menus) {
                returnMenu = i;
            }

            return menuMapper.menuToResponseDto(returnMenu);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @DeleteMapping("/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long ingredientId, @PathVariable("menuId") Long menuId) {
        try {
            Ingredient ingr = ingredientDao.getById(ingredientId);
            Collection<Menu> ingrMenus = ingr.getMenus();
            ingrMenus.removeIf(menu -> menu.getId().equals(menuId));
            ingr.setMenus(ingrMenus);
            ingredientDao.saveAndFlush(ingr);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }
}
