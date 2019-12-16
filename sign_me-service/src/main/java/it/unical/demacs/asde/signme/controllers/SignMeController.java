package it.unical.demacs.asde.signme.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.model.DTO.UserLoginDTO;
import it.unical.demacs.asde.signme.model.DTO.UserPictureUpdateDTO;
import it.unical.demacs.asde.signme.model.DTO.UserRegistrationDTO;
import it.unical.demacs.asde.signme.services.CourseService;
import it.unical.demacs.asde.signme.services.FaceRecognitionService;
import it.unical.demacs.asde.signme.services.LoginService;
import it.unical.demacs.asde.signme.services.UserPictureUpdateService;

@RestController
public class SignMeController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private FaceRecognitionService faceRecognitionService;
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserPictureUpdateService userPictureUpdateService;

	@CrossOrigin
	@PostMapping("/login")
	public User login(@RequestBody UserLoginDTO userLoginDTO) {
		return loginService.login(userLoginDTO);
	}

	@CrossOrigin
	@PostMapping("/register")
	public User register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
		return loginService.register(userRegistrationDTO);
	}

	@CrossOrigin
	@PostMapping("/pictureUpdate")
	public User uploadPicture (@RequestBody UserPictureUpdateDTO pictureUpdateDTO) {
		return null;
	}
	
	@CrossOrigin
	@GetMapping("/getStudentCourses")
	public List<Course> getStudentCourses(@RequestParam String email){
		return courseService.getStudentCourses(email);
	}
	
	@CrossOrigin
	@GetMapping("/getLecturerCourses")
	public List<Course> getLecturerCourses(@RequestParam String email){
		return courseService.getLecturerCourses(email);
	}
	
	@CrossOrigin
	@PostMapping("/createCourse")
	public String createCourse(@RequestBody CourseDTO courseDTO) {
		return courseService.createCourse(courseDTO);
	}
	
	
	
}
