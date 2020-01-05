package it.unical.demacs.asde.signme.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Invitation;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.InvitationDTO;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.InvitationDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class InvitationService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CourseDAO courseDAO;

	@Autowired
	private InvitationDAO InvitationDAO;

	public String createInvitation(InvitationDTO invitationDTO) {
		User student = userDAO.findById(invitationDTO.getEmail()).get();
		Course course = courseDAO.findById(invitationDTO.getCourse()).get();
		Invitation invitation = new Invitation();
		invitation.setStudent(student);
		invitation.setCourse(course.getCourseId());
		
		String key = student.getEmail() + course.getCourseId();
		invitation.setInvitationId(key);
		try {
			InvitationDAO.findById(key).get();
			System.out.println("ISCRIZIONE PRESENTE");
			return "failed";
		} catch (java.util.NoSuchElementException e) {
			InvitationDAO.save(invitation);
			System.out.println("ISCRIZIONE AVVENUTA");
			return "success";
		}
	}

	public Set<Invitation> getInvitation(Integer courseID) {
		return InvitationDAO.findInvitationsByCourse(courseID);
	}

}
