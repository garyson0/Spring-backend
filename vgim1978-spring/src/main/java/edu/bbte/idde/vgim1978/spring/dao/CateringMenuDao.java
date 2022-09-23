package edu.bbte.idde.vgim1978.spring.dao;

import edu.bbte.idde.vgim1978.spring.model.Menu;

import java.util.Collection;

public interface CateringMenuDao extends Dao<Menu> {
    Collection<Menu> findByName(String name);
}
