package it.unical.demacs.asde.signme.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Invitation;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.InvitationDTO;
import it.unical.demacs.asde.signme.model.DTO.UserRegistrationDTO;
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
		Invitation invitation = new Invitation();
		invitation.setEmail(invitationDTO.getEmail());
		invitation.setCourse(invitationDTO.getCourse());
		String key = invitationDTO.getEmail() + invitationDTO.getCourse();
		invitation.setInvitationId(key);
		InvitationDAO.save(invitation);
		return "success";
	}

	public Set<UserRegistrationDTO> getInvitation(Integer courseId) {
		Set<Invitation> invitations = InvitationDAO.findByCourse(courseId);

		Set<UserRegistrationDTO> students = new HashSet<>();

		for (Invitation invitation : invitations) {
			User u = (userDAO.findById(invitation.getEmail()).get());
			UserRegistrationDTO student = new UserRegistrationDTO();
			student.setEmail(u.getEmail());
			student.setFirstName(u.getFirstName());
			student.setLastName(u.getLastName());
			students.add(student);
		}
		return students;
	}

	public String confirmSubscription(InvitationDTO handleSubscriptionDTO) {
		User user = userDAO.findById(handleSubscriptionDTO.getEmail()).get();
		Course course = courseDAO.findById(handleSubscriptionDTO.getCourse()).get();
		Set<Course> followingCourse = user.getFollowingCourses();
		followingCourse.add(course);
		user.setFollowingCourses(followingCourse);
		userDAO.save(user);
		String invitationId = user.getEmail() + course.getCourseId();
		InvitationDAO.deleteById(invitationId);

		return "success";
	}

	public String deleteSubscription(InvitationDTO handleSubscriptionDTO) {

		String key = handleSubscriptionDTO.getEmail() + handleSubscriptionDTO.getCourse();
		InvitationDAO.deleteById(key);

		return "success";
	}

}
