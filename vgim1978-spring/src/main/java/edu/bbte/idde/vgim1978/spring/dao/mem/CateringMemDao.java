package edu.bbte.idde.vgim1978.spring.dao.mem;

import edu.bbte.idde.vgim1978.spring.dao.CateringMenuDao;
import edu.bbte.idde.vgim1978.spring.model.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
@Profile("mem")
public class CateringMemDao implements CateringMenuDao {

    private static Map<Long, Menu> memDb = new ConcurrentHashMap<>();
    private static AtomicLong currentId = new AtomicLong(0L);

    @Override
    public Menu saveAndFlush(Menu menu) {
        log.debug("Adding menu: " + menu);
        long id = currentId.getAndIncrement();
        menu.setId(id);
        memDb.put(id, menu);
        return menu;
    }

    public Menu update(Long id, Menu menu) {
        log.info("Updating menu with id: {}", id);
        if (memDb.containsKey(id)) {
            memDb.put(id, menu);
        }
        return menu;
    }

    @Override
    public Collection<Menu> findByName(String name) {
        log.debug("Getting menu with name: {}", name);
        Collection<Menu> returnMenus = new ArrayList<>();
        for (Map.Entry<Long, Menu> cme: memDb.entrySet()) {
            Menu check = cme.getValue();
            if (Objects.equals(check.getName(), name)) {
                returnMenus.add(check);
            }
        }
        log.debug("{}", returnMenus);
        return returnMenus;
    }

    @Override
    public Collection<Menu> findAll() {
        log.debug("All menus: {}",memDb.values());
        return memDb.values();
    }

    @Override
    public Menu getById(Long id) {
        log.debug("Getting menu with id: {}",id);
        return memDb.get(id);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Removing menu with id: {}", id);
        memDb.remove(id);
    }

}
