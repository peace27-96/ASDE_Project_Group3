package it.unical.demacs.asde.signme.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Invitation;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.AttendanceDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseCreationDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDeletionDTO;
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

	public Lecture createLecture(LectureDTO lectureDTO) {

		Course course = courseDAO.findById(lectureDTO.getCourse()).get();

		Lecture lecture = new Lecture();
		lecture.setDescription(lectureDTO.getDescription());
		lecture.setDate(lectureDTO.getDate());
		lecture.setCourse(course);

		lectureDAO.save(lecture);

		int id = lecture.getLectureId();

		lecture = lectureDAO.findById(id).get();

		return lecture;
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
				for (Course c : courses) {
					System.out.println("tutti i corsi ai quali l'utente " + user.getEmail()
							+ " è iscritto dopo l'eliminazione " + c.getCourseId());
				}
				System.out.println("userLecture");
				for (Lecture userLecture : newUserLectures) {
					System.out.println("userLecture" + userLecture.getLectureId());
				}
				user.setAttendedLectures(newUserLectures);
				user.setFollowingCourses(courses);
				userDAO.save(user);
			}
		}

		// delete foreign key from invitation
		Set<Invitation> invitations = invitationDAO.findInvitationsByCourse(courseDTO.getCourseId());
		for (Invitation invitation : invitations) {
			invitationDAO.delete(invitation);
		}
		// delete foreign key from lecture without student
		course = courseDAO.findById(courseDTO.getCourseId()).get();
		Set<Lecture> lectures = course.getLectures();
		for (Lecture lecture : lectures) {
			lectureDAO.deleteById(lecture.getLectureId());
		}
		// delete course
		courseDAO.deleteById(courseDTO.getCourseId());
		return "success";
	}

	public Set<User> getCourseStudents(String courseId) {
		Integer id = Integer.parseInt(courseId);
		Course course = courseDAO.findById(id).get();
		return course.getStudents();

	}

	public String deleteLecture(LectureDeletionDTO deleteLectureDTO) {
		lectureDAO.deleteById(deleteLectureDTO.getLectureId());
		return "success";
	}

	public Set<Lecture> getCourseLectures(String courseId) {
		Integer id = Integer.parseInt(courseId);
		System.out.println("CourseID: " + courseId);
		Set<Lecture> tmp = courseDAO.findById(id).get().getLectures();
		System.out.println(tmp.size());
		return tmp;

	}

	public Set<User> getLectureAttendances(String lectureId) {
		
		Lecture lecture = lectureDAO.findById(Integer.parseInt(lectureId)).get();
		return lecture.getStudents();
	}

	public String deleteAttendance(AttendanceDTO attendanceDTO) {
		User user = userDAO.findById(attendanceDTO.getEmail()).get();
		Set<Lecture> lectures = user.getAttendedLectures();
		Lecture lectureToDelete = null;
		for (Lecture lecture : lectures) {
			if (lecture.getLectureId().equals(attendanceDTO.getLectureId())) {
				lectureToDelete = lecture;
			}
		}
		if (lectureToDelete != null) {
			lectures.remove(lectureToDelete);
		}

		user.setAttendedLectures(lectures);
		userDAO.save(user);

		return "success";
	}

	public User addAttendance(AttendanceDTO attendanceDTO) {
		User user = userDAO.findById(attendanceDTO.getEmail()).get();
		Set<Lecture> lectures = user.getAttendedLectures();
		Lecture lectureToAdd = lectureDAO.findById(attendanceDTO.getLectureId()).get();

		lectures.add(lectureToAdd);

		user.setAttendedLectures(lectures);

		userDAO.save(user);

		return user;
	}

	public String getAttendancesNumber(String email, String strCourseId) {
		Integer courseId = Integer.parseInt(strCourseId);
		Course course = courseDAO.findById(courseId).get();
		User user = userDAO.findById(email).get();

		Set<Lecture> courseLectures = course.getLectures();

		Set<Lecture> userLectures = user.getAttendedLectures();

		int tot = courseLectures.size();
		int count = 0;

		for (Lecture cl : courseLectures) {
			for (Lecture ul : userLectures) {
				if (cl.getLectureId() == ul.getLectureId())
					count++;
			}
		}
		System.out.println("il corso " + courseId + " info: " + count + "/" + tot);
		return count + "/" + tot;
	}

}
