package it.unical.demacs.asde.signme.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import it.unical.demacs.asde.signme.model.SubscriptionRequest;

public interface SubscriptionRequestDAO extends CrudRepository<SubscriptionRequest, Integer> {

	
	Set<SubscriptionRequest> findSubscriptionRequestsByCourse(Integer courseId);
	
	
}
