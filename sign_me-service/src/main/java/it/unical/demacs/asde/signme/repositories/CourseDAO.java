package it.unical.demacs.asde.signme.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import it.unical.demacs.asde.signme.model.Course;

public interface CourseDAO extends CrudRepository<Course, Integer> {
	
	Set<Course> findCoursesByLecturerEmail(String email);
	
}
