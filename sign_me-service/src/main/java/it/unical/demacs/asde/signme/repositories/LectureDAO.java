package it.unical.demacs.asde.signme.repositories;

import org.springframework.data.repository.CrudRepository;

import it.unical.demacs.asde.signme.model.Lecture;

public interface LectureDAO extends CrudRepository<Lecture, Integer> {

}