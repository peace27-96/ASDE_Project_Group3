package it.unical.demacs.asde.signme.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unical.demacs.asde.signme.model.Course;

@Repository
public interface CourseDAO extends CrudRepository<Course, Integer> {

	Set<Course> findCoursesByLecturerEmail(String email);

}
