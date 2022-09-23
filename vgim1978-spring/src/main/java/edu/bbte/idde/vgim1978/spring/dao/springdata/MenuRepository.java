package edu.bbte.idde.vgim1978.spring.dao.springdata;

import edu.bbte.idde.vgim1978.spring.dao.CateringMenuDao;
import edu.bbte.idde.vgim1978.spring.model.Menu;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Profile("data")
@Repository
public interface MenuRepository extends CateringMenuDao, JpaRepository<Menu, Long> {

}
