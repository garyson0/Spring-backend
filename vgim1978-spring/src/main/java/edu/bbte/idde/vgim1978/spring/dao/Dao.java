package edu.bbte.idde.vgim1978.spring.dao;

import edu.bbte.idde.vgim1978.spring.model.BaseEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Profile("mem")
public interface Dao<T extends BaseEntity> {

    Collection<T> findAll();

    T saveAndFlush(T entity);

    T getById(Long id);

    void deleteById(Long id);

}
