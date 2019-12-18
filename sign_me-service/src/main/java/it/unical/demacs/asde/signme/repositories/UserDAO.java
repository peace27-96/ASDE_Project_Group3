package it.unical.demacs.asde.signme.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unical.demacs.asde.signme.model.User;

@Repository
public interface UserDAO extends CrudRepository<User, String> {

	User findUserByFirstName(String firstName);

}
