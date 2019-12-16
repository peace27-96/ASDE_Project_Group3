package it.unical.demacs.asde.signme.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class CourseService {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private CourseDAO courseDAO;
	
	public List<Course> getStudentCourses(String email) {

		User user = userDAO.findById(email).get();
		
		return new ArrayList<>(user.getFollowingCourses());
	}

	public List<Course> getLecturerCourses(String email) {

		return new ArrayList<>(courseDAO.findCoursesByLecturerEmail(email));
		
	}

	public String createCourse(CourseDTO courseDTO) {
		
		User lecturer = userDAO.findById(courseDTO.getEmail()).get();
		
		Course course = new Course();
		course.setSubject(courseDTO.getSubject());
		course.setLecturer(lecturer);
		
		courseDAO.save(course);
		
		return "success";
	}
	
	
}
