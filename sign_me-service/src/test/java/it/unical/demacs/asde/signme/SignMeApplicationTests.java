package it.unical.demacs.asde.signme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
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
		userDAO.save(new User("foo@foo.com", "foo", "malvio", "foo", null, null));
		User malvio = userDAO.findById("foo@foo.com").get();
		assertEquals(malvio.getFirstName(), "malvio");
	}

	@Test
	void userCoursesMtMWorks() {
		userDAO.save(new User("kiello@foo.com", "foo", "kiello", "pace", null, null));
		userDAO.save(new User("cris@foo.com", "foo", "cris", "dema", null, null));
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
		User kiello = new User("kiello@foo.com", "foo", "kiello", "pace", null, null);

		Lecture lecture1 = new Lecture(01, "Intro", null, null);
		Lecture lecture2 = new Lecture(02, "Pattern", null, null);

		Set<Lecture> lectures = new LinkedHashSet<Lecture>();
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
		Lecture lecture1 = new Lecture(1, "Intro", course, null);
		Lecture lecture2 = new Lecture(2, "Pattern", course, null);
		Lecture lecture3 = new Lecture(3, "Scrum", course, null);
		Lecture lecture4 = new Lecture(4, "DTO", course, null);

		Set<Lecture> lectures = new LinkedHashSet<Lecture>();
		lectures.add(lecture1);
		lectures.add(lecture2);
		lectures.add(lecture3);
		lectures.add(lecture4);

		course.setLectures(lectures);

		courseDAO.save(course);

		course = courseDAO.findById(1).get();

		for (Lecture lecture : course.getLectures()) {
			System.out.println(lecture.getDescription());
		}

		assertEquals(course.getLectures().size(), 4);
	}

}
