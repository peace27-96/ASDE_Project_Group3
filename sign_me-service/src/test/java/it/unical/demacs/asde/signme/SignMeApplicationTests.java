package it.unical.demacs.asde.signme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.LectureDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

// h2 database is needed to perform the tests

@SpringBootTest
class SignMeApplicationTests {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CourseDAO courseDAO;

	@Autowired
	private LectureDAO lectureDAO;

	@Test
	public void registerAndLogin() {
		User u = new User();
		u.setEmail("email@email");
		u.setPassword("asd");
		u.setFirstName("Luca");
		u.setLastName("Rossi");
		userDAO.save(u);
		u = userDAO.findById("email@email").get();
		assertEquals("email@email", u.getEmail());
	}

	@Test
	public void courseSubscription() {
		User u = userDAO.findById("email@email").get();
		// test course saving
		Course c = new Course();
		c.setSubject("Agile");
		courseDAO.save(c);
		c = courseDAO.findById(c.getCourseId()).get();
		assertEquals("Agile", c.getSubject());

		// test user subscription
		Set<Course> courses = u.getFollowingCourses();
		courses.add(c);
		u.setFollowingCourses(courses);
		userDAO.save(u);
		c = courseDAO.findById(c.getCourseId()).get();
		assertEquals(1, c.getStudents().size());
	}

	@Test
	public void lecturerCourses() {
		User u = userDAO.findById("email@email").get();
		Course c = new Course();
		c.setSubject("INGSW");
		c.setLecturer(u);
		courseDAO.save(c);
		u = userDAO.findById("email@email").get();
		assertEquals(u, c.getLecturer());
	}

	@Test
	public void courseLectures() {
		Course c = new Course();
		c.setSubject("Mobile");
		courseDAO.save(c);
		c = courseDAO.findById(c.getCourseId()).get();
		Lecture l = new Lecture();
		l.setCourse(c);
		lectureDAO.save(l);
		Set<Lecture> lecturesTest = lectureDAO.findByCourseCourseId(c.getCourseId());
		assertEquals(1, lecturesTest.size());
		l = lectureDAO.findById(l.getLectureId()).get();
		Set<User> users = l.getStudents();
		for (User user : users) {
			Set<Lecture> lectures = user.getAttendedLectures();
			Lecture lectureToDelete = null;
			for (Lecture lecture : lectures) {
				if (lecture.getLectureId().equals(l.getLectureId())) {
					lectureToDelete = lecture;
				}
			}
			if (lectureToDelete != null) {
				lectures.remove(lectureToDelete);
			}
			user.setAttendedLectures(lectures);
			userDAO.save(user);
		}
		lectureDAO.deleteById(l.getLectureId());
		lecturesTest = lectureDAO.findByCourseCourseId(c.getCourseId());
		assertEquals(0, lecturesTest.size());
	}

	@Test
	public void getCourseAvailable() {
		User prof = new User();
		prof.setEmail("prof@prof");
		userDAO.save(prof);
		User stud = new User();
		stud.setEmail("stud@stud");
		userDAO.save(stud);

		Course c = new Course();
		c.setSubject("Mobile");
		c.setLecturer(prof);
		courseDAO.save(c);

		Set<Course> courses = courseDAO.findCoursesAvailable(stud);
		assertEquals(1, courses.size());
	}

	@Test
	public void userAttendance() {
		User u = userDAO.findById("stud@stud").get();
		assertEquals("stud@stud", u.getEmail());
		Course c = new Course();
		c.setSubject("SSD");
		courseDAO.save(c);
		c = courseDAO.findById(c.getCourseId()).get();
		Lecture l = new Lecture();
		l.setCourse(c);
		lectureDAO.save(l);
		Set<Lecture> lectures = u.getAttendedLectures();
		lectures.add(l);
		u.setAttendedLectures(lectures);
		userDAO.save(u);
		l = lectureDAO.findById(l.getLectureId()).get();
		assertEquals(1, l.getStudents().size());
		u = userDAO.findById("stud@stud").get();
		lectures = u.getAttendedLectures();
		lectures.remove(l);
		u.setAttendedLectures(lectures);
		userDAO.save(u);
		l = lectureDAO.findById(l.getLectureId()).get();
		assertEquals(0, l.getStudents().size());
	}

}