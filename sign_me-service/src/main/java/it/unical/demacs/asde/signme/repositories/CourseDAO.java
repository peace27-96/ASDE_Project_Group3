package it.unical.demacs.asde.signme.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.unical.demacs.asde.signme.model.Course;

public interface CourseDAO extends CrudRepository<Course, Integer> {
	
}
