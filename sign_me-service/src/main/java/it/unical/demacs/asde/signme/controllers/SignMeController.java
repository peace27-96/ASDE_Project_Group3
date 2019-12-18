package it.unical.demacs.asde.signme.controllers;

import java.util.ArrayList;
import java.util.List;
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
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.SubscriptionRequest;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.CourseCreationDTO;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDTO;
import it.unical.demacs.asde.signme.model.DTO.SubscriptionRequestDTO;
import it.unical.demacs.asde.signme.model.DTO.UserLoginDTO;
import it.unical.demacs.asde.signme.model.DTO.UserPictureUpdateDTO;
import it.unical.demacs.asde.signme.model.DTO.UserRegistrationDTO;
import it.unical.demacs.asde.signme.services.CourseService;
import it.unical.demacs.asde.signme.services.LoginService;
import it.unical.demacs.asde.signme.services.SubscriptionRequestService;
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
	private SubscriptionRequestService subscriptionRequestService;

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
	public List<Course> getAllCourses() {
		return courseService.getAllCourses();
	}

	@GetMapping("/getStudentCourses")
	public List<Course> getStudentCourses(@RequestParam String email) {
		return courseService.getStudentCourses(email);
	}

	@GetMapping("/getLecturerCourses")
	public List<Course> getLecturerCourses(@RequestParam String email) {
		return courseService.getLecturerCourses(email);
	}

	@GetMapping("/getCourseLectures")
	public Set<Lecture> getCourseLectures(@RequestBody CourseDTO courseDTO) {
		return courseService.getCourseLectures(courseDTO);
	}

	@PostMapping("/createCourse")
	public String createCourse(@RequestBody CourseCreationDTO courseCreationDTO) {
		return courseService.createCourse(courseCreationDTO);
	}

	@PostMapping("/createLecture")
	public String createLecture(@RequestBody LectureDTO lectureDTO) {
		return courseService.createLecture(lectureDTO);
	}

	@PostMapping("/createSubscriptionRequest")
	public void createSubscriptionRequest(SubscriptionRequestDTO subscriptionRequestDTO) {
		subscriptionRequestService.createSubscriptionRequest(subscriptionRequestDTO);
	}

	@GetMapping("getSubscriptionRequests")
	public Set<SubscriptionRequest> getSubscriptionRequests(CourseDTO courseDTO) {
		return subscriptionRequestService.getSubscriptionRequests(courseDTO);
	}

	@PostMapping("/updateProfilePicture")
	public String updateProfilePicture(@RequestBody MultipartFile file) {
		return uploadImageService.updateProfilePicture(file);
	}

	@PostMapping("/uploadAttendacesPicture")
	public ArrayList<String> uploadAttendacesPicture(@RequestBody MultipartFile file) {
		return uploadImageService.uploadAttendacesPicture(file);
	}

}
