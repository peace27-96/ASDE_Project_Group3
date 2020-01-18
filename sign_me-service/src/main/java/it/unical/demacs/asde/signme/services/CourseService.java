package it.unical.demacs.asde.signme.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Invitation;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.Material;
import it.unical.demacs.asde.signme.model.Notice;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.AttendanceDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseCreationDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseInfoDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDeletionDTO;
import it.unical.demacs.asde.signme.model.DTO.LecturesInfoDTO;
import it.unical.demacs.asde.signme.model.DTO.NoticeDTO;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.InvitationDAO;
import it.unical.demacs.asde.signme.repositories.LectureDAO;
import it.unical.demacs.asde.signme.repositories.MaterialDAO;
import it.unical.demacs.asde.signme.repositories.NoticeDAO;
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

	@Autowired
	private NoticeDAO noticeDAO;

	@Autowired
	private MaterialDAO materialDAO;

	private static String FOLDER = "uploads/material/";

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
		Lecture lecture = lectureDAO.findById(deleteLectureDTO.getLectureId()).get();
		Set<User> users = lecture.getStudents();

		for (User user : users) {
			Set<Lecture> lectures = user.getAttendedLectures();
			Lecture lectureToDelete = null;
			for (Lecture l : lectures) {
				if (l.getLectureId().equals(deleteLectureDTO.getLectureId())) {
					lectureToDelete = l;
				}
			}
			if (lectureToDelete != null) {
				lectures.remove(lectureToDelete);
			}
			user.setAttendedLectures(lectures);
			userDAO.save(user);
		}
		lectureDAO.deleteById(deleteLectureDTO.getLectureId());
		return "success";
	}

	public LecturesInfoDTO getCourseLectures(String courseId) {
		Integer id = Integer.parseInt(courseId);
		System.out.println("CourseID: " + courseId);
		LecturesInfoDTO lecturesInfoDTO = new LecturesInfoDTO();
		Course c = courseDAO.findById(id).get();
		lecturesInfoDTO.setLectures(c.getLectures());
		lecturesInfoDTO.setLecturer(c.getLecturer().getEmail());
		System.out.println(lecturesInfoDTO.getLectures().size());
		return lecturesInfoDTO;

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

	private boolean isFile(MultipartFile file) {
		String extension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename());
		return ((extension.equals("pdf") || extension.equals("ppt") || extension.equals("pptx")
				|| extension.equals("zip")));
	}

	public Material uploadMaterial(MultipartFile file) {
		try {
			if (!isFile(file))
				return null;
			String fileName = file.getOriginalFilename();

			int courseId = Integer.parseInt(fileName.split("_")[0]);

			String description = fileName.split("_")[1];

			byte[] bytes = file.getBytes();
			Path path = Paths.get(FOLDER + fileName);
			File currFile = new File(FOLDER + fileName);
			currFile.delete();
			Files.write(path, bytes);

			Course course = courseDAO.findById(courseId).get();
			Material material = new Material();
			material.setDescription(description);
			material.setMaterialPath(path.toString());
			material.setCourse(course);
			materialDAO.save(material);

			return material;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String deleteMaterial(NoticeDTO materialDTO) {
		//File currFile = new File(FOLDER + materialDTO.getNoticeDescription());
		System.out.println(materialDTO.getNoticeDescription() + " " + materialDTO.getCourseId());
		materialDAO.deleteById(materialDTO.getCourseId());
		return "success";
	}

	public Set<Material> getMaterials(String courseId) {
		return materialDAO.findMaterialsByCourseCourseId(Integer.parseInt(courseId));
	}

	public Notice createNotice(NoticeDTO noticeDTO) {
		String description = noticeDTO.getNoticeDescription();
		if (description.length() > 251)
			description = description.substring(0, 250);
		Course course = courseDAO.findById(noticeDTO.getCourseId()).get();
		Notice notice = new Notice();
		notice.setCourse(course);
		notice.setDescription(description);
		noticeDAO.save(notice);
		return notice;
	}

	public String deleteNotice(CourseDTO noticeDTO) {
		noticeDAO.deleteById(noticeDTO.getCourseId());
		return "success";
	}

	public Set<Notice> getNotices(String courseId) {
		return noticeDAO.findNoticesByCourseCourseId(Integer.parseInt(courseId));
	}

	public CourseInfoDTO getCourseInfo(String courseId) {
		LecturesInfoDTO lecturesInfoDTO = getCourseLectures(courseId);
		Set<User> users = getCourseStudents(courseId);
		Set<Notice> notices = getNotices(courseId);
		Set<Material> materials = getMaterials(courseId);
		CourseInfoDTO courseInfoDTO = new CourseInfoDTO();
		courseInfoDTO.setLecturesInfoDTO(lecturesInfoDTO);
		courseInfoDTO.setNotices(notices);
		courseInfoDTO.setUsers(users);
		courseInfoDTO.setMaterial(materials);
		return courseInfoDTO;
	}

}
