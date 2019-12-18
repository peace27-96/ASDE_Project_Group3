package it.unical.demacs.asde.signme.services;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.UserLoginDTO;
import it.unical.demacs.asde.signme.model.DTO.UserRegistrationDTO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class LoginService {

	@Autowired
	private UserDAO userDAO;

	public User login(UserLoginDTO userLoginDTO) {
		try {
			User user = userDAO.findById(userLoginDTO.getEmail()).get();
			if (user != null)
				if (user.getPassword().equals(userLoginDTO.getPassword())) {
					System.out.println(user.toString());
					return user;
				}
		} catch (NoSuchElementException e) {
			return null;

		}
		return null;
	}

	public User register(UserRegistrationDTO userRegistrationDTO) {
		try {
			userDAO.findById(userRegistrationDTO.getEmail()).get();
		} catch (NoSuchElementException e) {
			User u = new User();
			u.setEmail(userRegistrationDTO.getEmail());
			u.setPassword(userRegistrationDTO.getPassword());
			u.setFirstName(userRegistrationDTO.getFirstName());
			u.setLastName(userRegistrationDTO.getLastName());
			userDAO.save(u);
			return u;
		}
		return null;
	}

}
