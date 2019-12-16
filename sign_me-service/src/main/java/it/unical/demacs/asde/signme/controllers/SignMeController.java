package it.unical.demacs.asde.signme.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.UserLoginDTO;
import it.unical.demacs.asde.signme.model.UserPictureUpdateDTO;
import it.unical.demacs.asde.signme.model.UserRegistrationDTO;
import it.unical.demacs.asde.signme.services.LoginService;
import it.unical.demacs.asde.signme.services.RegistrationService;
import it.unical.demacs.asde.signme.services.UserPictureUpdateService;

@RestController
public class SignMeController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private RegistrationService registrationService;

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
		return registrationService.register(userRegistrationDTO);
	}

	@CrossOrigin
	@PostMapping("/pictureUpdate")
	public User uploadPicture (@RequestBody UserPictureUpdateDTO pictureUpdateDTO) {
		return null;
	}
	
}
