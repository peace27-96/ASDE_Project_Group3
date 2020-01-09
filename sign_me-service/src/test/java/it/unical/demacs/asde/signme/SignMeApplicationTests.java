package it.unical.demacs.asde.signme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Invitation;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.InvitationDAO;
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

	@Autowired
	private InvitationDAO invitationDAO;

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

		// delete foreign key from users
		Course course = null;
		Set<User> users = userDAO.findUsersByFollowingCoursesCourseId(c.getCourseId());
		for (User user : users) {
			courses = user.getFollowingCourses();
			for (Course cc : courses) {
				if (cc.getCourseId() == c.getCourseId()) {
					course = cc;
				}
			}
			if (course != null) {
				courses.remove(course);
				Set<Lecture> userLectures = user.getAttendedLectures();
				Set<Lecture> lectures = course.getLectures();
				Set<Lecture> lecturesToDelete = new HashSet<>();
				for (Lecture userLecture : userLectures) {
					for (Lecture lecture : lectures) {
						if (userLecture.getLectureId() != lecture.getLectureId()) {
							lecturesToDelete.add(lecture);
						}
					}
				}
				Set<Lecture> newUserLectures = new HashSet<Lecture>();
				for (Lecture userLecture : userLectures) {
					for (Lecture lectureToDelete : lecturesToDelete) {
						if (userLecture.getLectureId() != lectureToDelete.getLectureId()) {
							newUserLectures.add(userLecture);
						}
					}
				}
				user.setAttendedLectures(newUserLectures);
				user.setFollowingCourses(courses);
				userDAO.save(user);
			}
		}

		// delete foreign key from invitation
		Set<Invitation> invitations = invitationDAO.findInvitationsByCourse(c.getCourseId());
		for (Invitation invitation : invitations) {
			invitationDAO.delete(invitation);
		}
		// delete foreign key from lecture without student
		course = courseDAO.findById(c.getCourseId()).get();
		Set<Lecture> lectures = course.getLectures();
		for (Lecture lecture : lectures) {
			lectureDAO.deleteById(lecture.getLectureId());
		}
		// delete course
		courseDAO.deleteById(c.getCourseId());
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
		c = courseDAO.findById(c.getCourseId()).get();
		assertEquals(1, c.getLectures().size());
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
		c = courseDAO.findById(c.getCourseId()).get();
		assertEquals(0, c.getLectures().size());
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
	public void courseInvitation() {
		User u = userDAO.findById("stud@stud").get();
		assertEquals("stud@stud", u.getEmail());
		Course c = new Course();
		c.setSubject("ASD");
		courseDAO.save(c);
		Invitation invitation = new Invitation();
		invitation.setCourse(c.getCourseId());
		invitation.setStudent(u);
		invitation.setInvitationId(u.getEmail() + c.getCourseId());
		invitationDAO.save(invitation);
		u = userDAO.findById("stud@stud").get();
		assertEquals(1, u.getUserInvitations().size());
		String key = u.getEmail() + c.getCourseId();
		invitationDAO.deleteById(key);
		u = userDAO.findById("stud@stud").get();
		assertEquals(0, u.getUserInvitations().size());
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