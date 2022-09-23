package edu.bbte.idde.vgim1978.spring.controller;

import edu.bbte.idde.vgim1978.spring.controller.dto.outgoing.MenuResponseDto;
import edu.bbte.idde.vgim1978.spring.controller.exception.NotFoundException;
import edu.bbte.idde.vgim1978.spring.dao.CateringMenuDao;
import edu.bbte.idde.vgim1978.spring.dao.IngredientDao;
import edu.bbte.idde.vgim1978.spring.dao.springdata.MenuRepository;
import edu.bbte.idde.vgim1978.spring.model.CrudEvent;
import edu.bbte.idde.vgim1978.spring.model.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@RestController
@RequestMapping("/crud")
@Slf4j
@Profile("data")
public class CrudController {

    @Autowired
    private CateringMenuDao menuDao;

    @Autowired
    private MenuRepository menuJpa;

    @GetMapping("/{id}")
    public Collection<CrudEvent> getAllMuvelet(@PathVariable("id") Long id) {
        try {
            Menu menu = menuDao.getById(id);
            return menu.getEvents();
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }
}
