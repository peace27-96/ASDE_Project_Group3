package it.unical.demacs.asde.signme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.unical.demacs.asde.signme.model.*;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@SpringBootTest
class SignMeApplicationTests {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private CourseDAO courseDAO;

	@Test
	void userDAOWorks() {
		userDAO.save(new User("foo@foo.com", "foo", "malvio", "foo"));
		User malvio = userDAO.findById("foo@foo.com").get();
		assertEquals(malvio.getFirstName(), "malvio");
	}

	@Test
	void userCoursesMtMWorks() {
		userDAO.save(new User("kiello@foo.com", "foo", "kiello", "pace"));
		userDAO.save(new User("cris@foo.com", "foo", "cris", "dema"));
		User kiello = userDAO.findUserByFirstName("kiello");
		User cris = userDAO.findUserByFirstName("cris");
		
		assertEquals(kiello.getFirstName(), "kiello");
		assertEquals(cris.getLastName(), "dema");
		
		Set<Course> courses = new HashSet<>();
		Course agile = new Course();
		agile.setSubject("Agile");
		agile.setCourseId(1);
		
		Course secure = new Course();
		secure.setSubject("Secure");
		secure.setCourseId(2);
		
		courses.add(agile);
		courses.add(secure);
		
		cris.setFollowingCourses(courses);
		
		userDAO.save(cris);
		
		cris = userDAO.findUserByFirstName("cris");
		
//		assertEquals(cris.getFollowingCourses().size(), 2);
		
	}
	
}
