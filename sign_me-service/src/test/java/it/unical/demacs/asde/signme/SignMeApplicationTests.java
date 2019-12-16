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

	@Test
	void userLecturesMtMWorks() {
		User kiello = new User("kiello@foo.com", "foo", "kiello", "pace");
		User cris = new User("cris@foo.com", "foo", "cris", "dema");

		Lecture lecture1 = new Lecture(01, "Intro", null);
		Lecture lecture2 = new Lecture(02, "Pattern", null);

		Set<Lecture> lectures = new HashSet<Lecture>();
		lectures.add(lecture1);
		lectures.add(lecture2);

		kiello.setAttendedLectures(lectures);

		userDAO.save(kiello);

		kiello = userDAO.findUserByFirstName("kiello");

		assertEquals(kiello.getAttendedLectures().size(), 2);
	}

	@Test
	void lecturesCoursesWorks() {
		Course course = new Course(1, "Agile", null, null);
		Lecture lecture1 = new Lecture(1, "Intro", course);
		Lecture lecture2 = new Lecture(2, "Pattern", course);

		List<Lecture> lectures = new ArrayList<Lecture>();
		lectures.add(lecture1);
		lectures.add(lecture2);

		course.setLectures(lectures);

		courseDAO.save(course);

		course = courseDAO.findById(1).get();

		assertEquals(course.getLectures().size(), 2);
	}

}
