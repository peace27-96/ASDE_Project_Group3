package it.unical.demacs.asde.signme.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.User;

@Repository
public interface CourseDAO extends CrudRepository<Course, Integer> {

	Set<Course> findByLecturerEmail(String email);

	@Query("FROM Course c WHERE c.lecturer!=:user")
	Set<Course> findCoursesAvailable(User user);
	
}
