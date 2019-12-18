package it.unical.demacs.asde.signme.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unical.demacs.asde.signme.model.Material;

@Repository
public interface MaterialDAO extends CrudRepository<Material, Integer> {

}
