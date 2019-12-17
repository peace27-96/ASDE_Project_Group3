package it.unical.demacs.asde.signme.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.SubscriptionRequest;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.model.DTO.CourseDTO;
import it.unical.demacs.asde.signme.model.DTO.SubscriptionRequestDTO;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.SubscriptionRequestDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class SubscriptionRequestService {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private SubscriptionRequestDAO subscriptionRequestDAO;
	
	public void createSubscriptionRequest(SubscriptionRequestDTO subscriptionRequestDTO) {

		User student = userDAO.findById(subscriptionRequestDTO.getEmail()).get();
		Course course = courseDAO.findById(subscriptionRequestDTO.getCourse()).get();
		
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
		subscriptionRequest.setStudent(student.getEmail());
		subscriptionRequest.setCourse(course.getCourseId());
		
		subscriptionRequestDAO.save(subscriptionRequest);
		
	}

	public Set<SubscriptionRequest> getSubscriptionRequests(CourseDTO courseDTO) {

		return subscriptionRequestDAO.findSubscriptionRequestsByCourse(courseDTO.getCourseId());
		
	}

	
	
	
}
