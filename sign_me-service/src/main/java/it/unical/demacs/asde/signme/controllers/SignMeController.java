package it.unical.demacs.asde.signme.controllers;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.model.DTO.LectureDTO;
import it.unical.demacs.asde.signme.model.DTO.UserLoginDTO;
import it.unical.demacs.asde.signme.model.DTO.UserPictureUpdateDTO;
import it.unical.demacs.asde.signme.model.DTO.UserRegistrationDTO;
import it.unical.demacs.asde.signme.services.CourseService;
import it.unical.demacs.asde.signme.services.FaceRecognitionService;
import it.unical.demacs.asde.signme.services.LoginService;
import it.unical.demacs.asde.signme.services.UploadImageService;

@CrossOrigin("*")
@RestController
public class SignMeController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private FaceRecognitionService faceRecognitionService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UploadImageService uploadImageService;

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

	@GetMapping("/getStudentCourses")
	public List<Course> getStudentCourses(@RequestParam String email) {
		return courseService.getStudentCourses(email);
	}

	@GetMapping("/getLecturerCourses")
	public List<Course> getLecturerCourses(@RequestParam String email) {
		return courseService.getLecturerCourses(email);
	}

	@PostMapping("/createCourse")
	public String createCourse(@RequestBody CourseDTO courseDTO) {
		return courseService.createCourse(courseDTO);
	}

	@PostMapping("/createLecture")
	public String createLecture(@RequestBody LectureDTO lectureDTO) {
		return courseService.createLecture(lectureDTO);
	}

	@PostMapping(value = "/updatePicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void uploadFile(@RequestBody MultipartFile file) {
		uploadImageService.updatePicture(file);
	}

}
