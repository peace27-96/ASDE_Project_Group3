package it.unical.demacs.asde.signme.services;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.UserLoginDTO;
import it.unical.demacs.asde.signme.model.DTO.UserRegistrationDTO;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class LoginService {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private CourseDAO courseDAO;

	public User login(UserLoginDTO userLoginDTO) {
		try {
			User user = userDAO.findById(userLoginDTO.getEmail()).get();
			if (user != null)
				if (user.getPassword().equals(userLoginDTO.getPassword())) {
					Set<Course> courses = courseDAO.findByLecturerEmail(user.getEmail());
					user.setCreatedCourses(courses);
					for (Course course : user.getCreatedCourses()) {
						course.setLectures(new HashSet<>());
					}
					return user;
				}
		} catch (NoSuchElementException e) {
			return null;

		}
		return null;
	}

	public User register(UserRegistrationDTO userRegistrationDTO) {
		try {
			userDAO.findById(userRegistrationDTO.getEmail()).get();
		} catch (NoSuchElementException e) {
			User u = new User();
			u.setEmail(userRegistrationDTO.getEmail());
			u.setPassword(userRegistrationDTO.getPassword());
			u.setFirstName(userRegistrationDTO.getFirstName());
			u.setLastName(userRegistrationDTO.getLastName());
			userDAO.save(u);
			return u;
		}
		return null;
	}

}
