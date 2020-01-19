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

		return user.getFollowingCourses();
	}

	public Set<Course> getLecturerCourses(String email) {
		return courseDAO.findByLecturerEmail(email);
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
		Course course = courseDAO.findById(courseDTO.getCourseId()).get();
		Set<User> users = userDAO.findUsersByFollowingCoursesCourseId(courseDTO.getCourseId());
		for (User user : users) {
			Set<Course> courses = user.getFollowingCourses();
			for (Course c : courses) {
				if (c.getCourseId() == courseDTO.getCourseId()) {
					course = c;
				}
			}
			// removing lectures and attendances
			courses.remove(course);
			Set<Lecture> userLectures = user.getAttendedLectures();
			Set<Lecture> lectures = lectureDAO.findByCourseCourseId(course.getCourseId());
			Set<Lecture> updatedAttendances = new HashSet<>();
			for (Lecture userLecture : userLectures) {
				for (Lecture lecture : lectures) {
					if (userLecture.getLectureId() != lecture.getLectureId()) {
						updatedAttendances.add(lecture);
					}
				}
			}
			user.setAttendedLectures(updatedAttendances);
			user.setFollowingCourses(courses);
			userDAO.save(user);

		}

		// delete foreign key from invitation
		Set<Invitation> invitations = invitationDAO.findByCourse(courseDTO.getCourseId());
		for (Invitation invitation : invitations) {
			invitationDAO.delete(invitation);
		}
		// delete foreign key from material
		Set<Material> materials = materialDAO.findMaterialsByCourseCourseId(courseDTO.getCourseId());
		for (Material material : materials) {
			materialDAO.delete(material);
		}
		// delete foreign key from notice
		Set<Notice> notices = noticeDAO.findNoticesByCourseCourseId(courseDTO.getCourseId());
		for (Notice notice : notices) {
			noticeDAO.delete(notice);
		}
		// delete foreign key from lecture without student
		Set<Lecture> lectures = lectureDAO.findByCourseCourseId(course.getCourseId());
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

	public Set<Lecture> getCourseLectures(String courseId) {
		Integer id = Integer.parseInt(courseId);
		return lectureDAO.findByCourseCourseId(id);
	}

	public Set<User> getLectureAttendances(String lectureId) {
		Lecture lecture = lectureDAO.findById(Integer.parseInt(lectureId)).get();
		for (User student : lecture.getStudents())
			student.setCreatedCourses(new HashSet<>());
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
		Set<Course> createdCourses = courseDAO.findByLecturerEmail(attendanceDTO.getEmail());
		user.setCreatedCourses(createdCourses);
		Set<Lecture> lectures = user.getAttendedLectures();
		Lecture lectureToAdd = lectureDAO.findById(attendanceDTO.getLectureId()).get();
		lectures.add(lectureToAdd);
		user.setAttendedLectures(lectures);

		userDAO.save(user);

		user.setCreatedCourses(new HashSet<>());
		return user;
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
		// File currFile = new File(FOLDER + materialDTO.getNoticeDescription());
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
		Course c = courseDAO.findById(Integer.parseInt(courseId)).get();
		Set<Lecture> lectures = getCourseLectures(courseId);
		Set<User> users = getCourseStudents(courseId);
		for (User user : users) {
			user.setCreatedCourses(new HashSet<>());
		}

		Set<Notice> notices = getNotices(courseId);
		Set<Material> materials = getMaterials(courseId);
		CourseInfoDTO courseInfoDTO = new CourseInfoDTO();
		courseInfoDTO.setLecturer(c.getLecturer().getEmail());
		courseInfoDTO.setLectures(lectures);
		courseInfoDTO.setNotices(notices);
		courseInfoDTO.setUsers(users);
		courseInfoDTO.setMaterial(materials);
		return courseInfoDTO;
	}

}
