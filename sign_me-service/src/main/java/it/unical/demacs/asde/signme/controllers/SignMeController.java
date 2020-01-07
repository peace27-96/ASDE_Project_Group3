package it.unical.demacs.asde.signme.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Invitation;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.AttendanceDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseCreationDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDeletionDTO;
import it.unical.demacs.asde.signme.model.DTO.HandleSubscriptionDTO;
import it.unical.demacs.asde.signme.model.DTO.InvitationDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDTO;
import it.unical.demacs.asde.signme.model.DTO.UserDTO;
import it.unical.demacs.asde.signme.model.DTO.UserLoginDTO;
import it.unical.demacs.asde.signme.model.DTO.UserPictureUpdateDTO;
import it.unical.demacs.asde.signme.model.DTO.UserRegistrationDTO;
import it.unical.demacs.asde.signme.services.CourseService;
import it.unical.demacs.asde.signme.services.InvitationService;
import it.unical.demacs.asde.signme.services.LoginService;
import it.unical.demacs.asde.signme.services.UploadImageService;

@CrossOrigin("*")
@RestController
public class SignMeController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UploadImageService uploadImageService;
	@Autowired
	private InvitationService invitationService;

	@PostMapping("/login")
	public User login(@RequestBody UserLoginDTO userLoginDTO) {
		return loginService.login(userLoginDTO);
	}

	@PostMapping("/register")
	public User register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
		return loginService.register(userRegistrationDTO);
	}

	@PostMapping("/pictureUpdate")
	public User uploadPicture(@RequestBody UserPictureUpdateDTO pictureUpdateDTO) {
		return null;
	}

	@GetMapping("/getAllCourses")
	public Set<Course> getAllCourses() {
		System.out.println("getAllCourses");
		return courseService.getAllCourses();
	}

	@PostMapping("/getStudentCourses")
	public Set<Course> getStudentCourses(@RequestBody UserDTO userDTO) {
		return courseService.getStudentCourses(userDTO.getEmail());
	}

	@GetMapping("/getCourseStudents")
	public Set<User> getCourseStudents(@RequestParam String courseId) {
		return courseService.getCourseStudents(courseId);
	}

	@GetMapping("/getLecturerCourses")
	public Set<Course> getLecturerCourses(@RequestParam String email) {
		return courseService.getLecturerCourses(email);
	}

	@GetMapping("/getCourseLectures")
	public Set<Lecture> getCourseLectures(@RequestParam String courseId) {
		return courseService.getCourseLectures(courseId);
	}

	@PostMapping("/getAllCoursesAvailable")
	public Set<Course> getAllCoursesAvailable(@RequestBody UserDTO userDTO) {
		return courseService.getAllCoursesAvailable(userDTO.getEmail());
	}

	@PostMapping("/createCourse")
	public String createCourse(@RequestBody CourseCreationDTO courseCreationDTO) {
		return courseService.createCourse(courseCreationDTO);
	}

	@PostMapping("/createLecture")
	public Lecture createLecture(@RequestBody LectureDTO lectureDTO) {
		return courseService.createLecture(lectureDTO);
	}

	@PostMapping("/createSubscriptionRequest")
	public String createInvitation(@RequestBody InvitationDTO InvitationDTO) {
		return invitationService.createInvitation(InvitationDTO);
	}

	@PostMapping("/getSubscriptionRequests")
	public Set<Invitation> getInvitations(@RequestBody CourseDTO courseDTO) {
		return invitationService.getInvitation(courseDTO.getCourseId());
	}

	@PostMapping("/updateProfilePicture")
	public String updateProfilePicture(@RequestBody MultipartFile file) {
		return uploadImageService.updateProfilePicture(file);
	}

	@PostMapping("/uploadAttendacesPicture")
	public Set<User> uploadAttendacesPicture(@RequestBody MultipartFile file) {
		return uploadImageService.uploadAttendacesPicture(file);
	}

	@PostMapping("/confirmSubscription")
	public String confirmSubscription(@RequestBody HandleSubscriptionDTO handleSubscriptionDTO) {
		return invitationService.confirmSubscription(handleSubscriptionDTO);
	}

	@PostMapping("/deleteSubscription")
	public String deleteSubscription(@RequestBody HandleSubscriptionDTO handleSubscriptionDTO) {
		return invitationService.deleteSubscription(handleSubscriptionDTO);
	}

	@PostMapping("/deleteCourse")
	public String deleteCourse(@RequestBody CourseDTO courseDTO) {
		return courseService.deleteCourse(courseDTO);
	}

	@PostMapping("/deleteLecture")
	public String deleteLecture(@RequestBody LectureDeletionDTO deleteLectureDTO) {
		return courseService.deleteLecture(deleteLectureDTO);
	}
	
	@GetMapping("/getLectureAttendances")
	public Set<User> getLectureAttendances(@RequestParam String lectureId) {
		return courseService.getLectureAttendances(lectureId);
	}
	
	@PostMapping("/deleteAttendance")
	public String deleteAttendance(@RequestBody AttendanceDTO attendanceDTO) {
		return courseService.deleteAttendance(attendanceDTO);
	}

	@PostMapping("/addAttendance")
	public User addAttendance(@RequestBody AttendanceDTO attendanceDTO) {
		return courseService.addAttendance(attendanceDTO);
	}
	
	@GetMapping("/getAttendancesNumber")
	public String getAttendancesNumber(@RequestParam String email, @RequestParam String courseId) {
		return courseService.getAttendancesNumber(email, courseId);
	}
}
