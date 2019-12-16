package it.unical.demacs.asde.signme.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.UserPictureUpdateDTO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class UserPictureUpdateService {

	@Autowired
	private UserDAO userDAO;
	
	public User updatePicture(UserPictureUpdateDTO user) {
		
		User u = userDAO.findById(user.getEmail()).get();
		
		// upload picture
		
		return u;
	}
	
}
