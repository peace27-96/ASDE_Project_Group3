package it.unical.demacs.asde.signme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.unical.demacs.asde.signme.model.*;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.LectureDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@SpringBootTest
class SignMeApplicationTests {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private LectureDAO lectureDAO;

	@Test
	void userDAOWorks() {
		userDAO.save(new User("foo@foo.com", "foo", "malvio", "foo", null, null, null));
		User malvio = userDAO.findById("foo@foo.com").get();

		try {
			User cris = userDAO.findById("cris@cris.com").get();
		} catch (NoSuchElementException e) {
		}

		assertEquals(malvio.getFirstName(), "malvio");
	}

	@Test
	void courseDAOWorks() {

		User cris = new User("cris@gmail.com", "bu", "Cristian", "De Marco", new HashSet<>(), new HashSet<>(), null);

		Set<Course> courses = new HashSet<>();
		
		Course agile = new Course();
		agile.setLecturer(cris);
		agile.setSubject("Agile");
		
		courses.add(agile);
		
		cris.setCreatedCourses(courses);

		userDAO.save(cris);
		
		List<Course> crisCourses = new ArrayList<>(courseDAO.findCoursesByLecturerEmail(cris.getEmail()));
		assertEquals(1, crisCourses.size());
		
		
		Course secure = new Course();
		secure.setLecturer(cris);
		courseDAO.save(secure);
		
		crisCourses = new ArrayList<>(courseDAO.findCoursesByLecturerEmail(cris.getEmail()));
		assertEquals(2, crisCourses.size());
		
	}

	@Test
	void userCoursesMtMWorks() {
		userDAO.save(new User("kiello@foo.com", "foo", "kiello", "pace", null, null, null));
		userDAO.save(new User("cris@foo.com", "foo", "cris", "dema", null, null, null));
		User kiello = userDAO.findUserByFirstName("kiello");
		User cris = userDAO.findUserByFirstName("cris");

		assertEquals(kiello.getFirstName(), "kiello");
		assertEquals(cris.getLastName(), "dema");

		Set<Course> courses = new LinkedHashSet<Course>();
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

	@Test
	void userLecturesMtMWorks() {
		User kiello = new User("kiello@foo.com", "foo", "kiello", "pace", null, null, null);

		Lecture lecture1 = new Lecture();
		Lecture lecture2 = new Lecture();
		
		lecture1.setDescription("Scrum");
		lecture2.setDescription("Bu");
		
		lectureDAO.save(lecture1);
		lectureDAO.save(lecture2);
		
		Iterable<Lecture> lectures = lectureDAO.findAll();
		
		for (Lecture lecture : lectures) {
			System.out.println(lecture.getLectureId() + " " + lecture.getDescription());
		}

	}


}
