package it.unical.demacs.asde.signme.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.UserRegistrationDTO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class RegistrationService {

	@Autowired
	private UserDAO userDAO;
	
	public User register(UserRegistrationDTO userRegistrationDTO) {
		
		
		
		return null;
	}
	
}
