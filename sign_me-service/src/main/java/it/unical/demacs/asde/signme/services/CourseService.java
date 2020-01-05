package it.unical.demacs.asde.signme.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Invitation;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.HandleSubscriptionDTO;
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
	private InvitationDAO InvitationDAO;

	public List<Course> getStudentCourses(String email) {

		User user = userDAO.findById(email).get();

		return new ArrayList<>(user.getFollowingCourses());
	}

	public List<Course> getLecturerCourses(String email) {

		return new ArrayList<>(courseDAO.findCoursesByLecturerEmail(email));

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

	public List<Course> getAllCourses() {

		Iterable<Course> allCourses = courseDAO.findAll();
		List<Course> courses = new ArrayList<>();
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

	public String confirmSubscription(HandleSubscriptionDTO handleSubscriptionDTO) {
		System.out.println(handleSubscriptionDTO.getStudent() + " " + handleSubscriptionDTO.getCourseId());
		User user = userDAO.findById(handleSubscriptionDTO.getStudent()).get();
		Course course = courseDAO.findById(handleSubscriptionDTO.getCourseId()).get();
		Set<Course> followingCourse = user.getFollowingCourses();
		followingCourse.add(course);
		user.setFollowingCourses(followingCourse);
		userDAO.save(user);

		Set<User> students = course.getStudents();
		students.add(user);
		course.setStudents(students);
		courseDAO.save(course);

		String key = user.getEmail() + course.getCourseId();
		Invitation invitation = InvitationDAO.findById(key).get();
		invitation.setPending(false);

		InvitationDAO.save(invitation);

		Course course2 = courseDAO.findById(handleSubscriptionDTO.getCourseId()).get();
		System.out.println("numero degli studenti che seguono il corso " + course2.getStudents().size());
		return "success";
	}

	public String deleteSubscription(HandleSubscriptionDTO handleSubscriptionDTO) {
		System.out.println(handleSubscriptionDTO.getStudent() + " " + handleSubscriptionDTO.getCourseId());

		String key = handleSubscriptionDTO.getStudent() + handleSubscriptionDTO.getCourseId();
		InvitationDAO.deleteById(key);

		return "success";
	}
}
