package it.unical.demacs.asde.signme.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Invitation;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.CourseCreationDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDTO;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.InvitationDAO;
import it.unical.demacs.asde.signme.repositories.LectureDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class CourseService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CourseDAO courseDAO;

	@Autowired
	private LectureDAO lectureDAO;

	@Autowired
	private InvitationDAO invitationDAO;

	public Set<Course> getStudentCourses(String email) {

		User user = userDAO.findById(email).get();

		return new HashSet<>(user.getFollowingCourses());
	}

	public Set<Course> getLecturerCourses(String email) {

		return new HashSet<>(courseDAO.findCoursesByLecturerEmail(email));

	}

	public String createCourse(CourseCreationDTO courseCreationDTO) {

		User lecturer = userDAO.findById(courseCreationDTO.getEmail()).get();

		Course course = new Course();
		course.setSubject(courseCreationDTO.getSubject());
		course.setLecturer(lecturer);

		courseDAO.save(course);

		return course.getCourseId().toString();

	}

	public String createLecture(LectureDTO lectureDTO) {

		Course course = courseDAO.findById(lectureDTO.getCourse()).get();

		Lecture lecture = new Lecture();
		lecture.setDescription(lectureDTO.getDescription());
		lecture.setDate(lectureDTO.getDate());
		lecture.setCourse(course);

		lectureDAO.save(lecture);

		return "success";
	}

	public Set<Course> getAllCourses() {

		Iterable<Course> allCourses = courseDAO.findAll();
		Set<Course> courses = new HashSet<Course>();
		for (Course course : allCourses)
			courses.add(course);
		return courses;

	}

	public Set<Lecture> getCourseLectures(CourseDTO courseDTO) {
		return courseDAO.findById(courseDTO.getCourseId()).get().getLectures();

	}

	public Set<Course> getAllCoursesAvailable(String email) {
		User user = userDAO.findById(email).get();
		return courseDAO.findCoursesAvailable(user);
	}

	public String deleteCourse(CourseDTO courseDTO) {
		// delete foreign key from users
		Course course = null;
		Set<User> users = userDAO.findUsersByFollowingCoursesCourseId(courseDTO.getCourseId());
		for (User user : users) {
			System.out.println("utente iscritto " + user.getEmail());
			Set<Course> courses = user.getFollowingCourses();
			for (Course c : courses) {
				System.out.println("tutti i corsi ai quali l'utente " + user.getEmail()
						+ " è iscritto prima dell'eliminazione " + c.getCourseId());
				if (c.getCourseId() == courseDTO.getCourseId()) {
					course = c;
				}
			}
			if (course != null) {
				courses.remove(course);
				for (Course c : courses) {
					System.out.println("tutti i corsi ai quali l'utente " + user.getEmail()
							+ " è iscritto dopo l'eliminazione " + c.getCourseId());
				}
				user.setFollowingCourses(courses);
				userDAO.save(user);
			}
		}
		// delete foreign key from invitation
		Set<Invitation> invitations = invitationDAO.findInvitationsByCourse(courseDTO.getCourseId());
		for (Invitation invitation : invitations) {
			invitationDAO.delete(invitation);
		}
		// delete course
		courseDAO.deleteById(courseDTO.getCourseId());
		return "success";
	}
}
