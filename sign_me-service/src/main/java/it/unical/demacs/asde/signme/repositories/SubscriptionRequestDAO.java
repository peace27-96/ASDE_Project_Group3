package it.unical.demacs.asde.signme.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unical.demacs.asde.signme.model.SubscriptionRequest;

@Repository
public interface SubscriptionRequestDAO extends CrudRepository<SubscriptionRequest, Integer> {

	
	Set<SubscriptionRequest> findSubscriptionRequestsByCourse(Integer courseId);
	
	
}
