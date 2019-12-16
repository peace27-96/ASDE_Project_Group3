package it.unical.demacs.asde.signme.repositories;
import org.springframework.data.repository.CrudRepository;

import it.unical.demacs.asde.signme.model.User;

public interface UserDAO extends CrudRepository<User, String> {
	
	User findUserByFirstName(String firstName);

}
