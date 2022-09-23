package edu.bbte.idde.vgim1978.spring.controller;

import edu.bbte.idde.vgim1978.spring.controller.dto.incoming.MenuCreationDto;
import edu.bbte.idde.vgim1978.spring.controller.dto.outgoing.MenuResponseDto;
import edu.bbte.idde.vgim1978.spring.controller.exception.NotFoundException;
import edu.bbte.idde.vgim1978.spring.controller.mapper.MenuMapper;
import edu.bbte.idde.vgim1978.spring.dao.CateringMenuDao;
import edu.bbte.idde.vgim1978.spring.model.CrudEvent;
import edu.bbte.idde.vgim1978.spring.model.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/menus")
@Slf4j
public class MenuController {

    @Autowired
    private CateringMenuDao menuDao;

    @Autowired
    private MenuMapper mapper;

    @GetMapping
    public Collection<MenuResponseDto> findAll() {
        return mapper.menuListToResponseDtoList(menuDao.findAll());
    }

    @GetMapping("/{id}")
    public MenuResponseDto findById(@PathVariable("id") Long id) {
        try {
            Menu menu = menuDao.getById(id);
            return mapper.menuToResponseDto(menu);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @GetMapping(params = "name")
    public Collection<MenuResponseDto> findByName(@RequestParam String name) {
        Collection<MenuResponseDto> resp = mapper.menuListToResponseDtoList(menuDao.findByName(name));
        if (resp == null) {
            throw new NotFoundException();
        }
        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuResponseDto create(@RequestBody @Valid MenuCreationDto menu) {
//        Menu createdMenu = menuDao.saveAndFlush(mapper.dtoToMenu(menu));
        Menu createdMenu = mapper.dtoToMenu(menu);
        CrudEvent crudEvent = new CrudEvent(LocalDateTime.now(),"create");


        Collection<CrudEvent> events = new ArrayList<CrudEvent>();
        events.add(crudEvent);
        createdMenu.setEvents(events);
        Menu createdMenu1 = menuDao.saveAndFlush(createdMenu);
        return mapper.menuToResponseDto(createdMenu1);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public MenuResponseDto update(@PathVariable("id") Long id,@RequestBody @Valid MenuCreationDto menu) {
        try {
            Menu toUpdate = mapper.dtoToMenu(menu);
            toUpdate.setId(id);
            Menu oldMenu = menuDao.getById(id);

            CrudEvent crudEvent = new CrudEvent(LocalDateTime.now(),"update");
            if(oldMenu.getEvents() == null) {
                Collection<CrudEvent> events = new ArrayList<CrudEvent>();
                events.add(crudEvent);
                toUpdate.setEvents(events);

            } else {
                Collection<CrudEvent> events = oldMenu.getEvents();
                events.add(crudEvent);
                toUpdate.setEvents(events);
            }

            Menu updatedMenu = menuDao.saveAndFlush(toUpdate);
            return mapper.menuToResponseDto(updatedMenu);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        try {
            menuDao.getById(id);
            menuDao.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }
}
