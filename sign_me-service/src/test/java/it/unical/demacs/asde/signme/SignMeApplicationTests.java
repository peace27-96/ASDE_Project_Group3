package it.unical.demacs.asde.signme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.unical.demacs.asde.signme.models.User;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@SpringBootTest
class SignMeApplicationTests {
	
	@Autowired
	private UserDAO userDAO;
	
	

	@Test
	void userDAOWorks() {
		userDAO.save(new User("foo@foo.com", "foo", "malvio", "foo"));
		User malvio = userDAO.findById("foo@foo.com").get();
		assertEquals(malvio.getFirst_name(), "malvio");
		
		
	}

}
