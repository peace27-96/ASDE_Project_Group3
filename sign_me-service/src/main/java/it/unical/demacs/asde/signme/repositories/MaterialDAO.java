package it.unical.demacs.asde.signme.repositories;

import org.springframework.data.repository.CrudRepository;

import it.unical.demacs.asde.signme.model.Material;

public interface MaterialDAO extends CrudRepository<Material, Integer> {

}
