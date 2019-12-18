package it.unical.demacs.asde.signme.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unical.demacs.asde.signme.model.Lecture;

@Repository
public interface LectureDAO extends CrudRepository<Lecture, Integer> {

}
