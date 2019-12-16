package it.unical.demacs.asde.signme.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.UserLoginDTO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class LoginService {

	@Autowired
	private UserDAO userDAO;
	
	public User login(UserLoginDTO userLoginDTO) {
		
		User user = userDAO.findById(userLoginDTO.getEmail()).get();
		
		System.out.println("email " + user.getEmail());
		
		return user;
	}

}
