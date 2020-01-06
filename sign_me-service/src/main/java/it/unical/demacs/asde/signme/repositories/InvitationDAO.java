package it.unical.demacs.asde.signme.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unical.demacs.asde.signme.model.Invitation;

@Repository
public interface InvitationDAO extends CrudRepository<Invitation, String> {

	@Query("FROM Invitation i WHERE i.course=:courseId AND i.pending is true")
	Set<Invitation> findAvailableInvitationsByCourse(Integer courseId);

	Set<Invitation> findInvitationsByCourse(Integer courseId);

}
