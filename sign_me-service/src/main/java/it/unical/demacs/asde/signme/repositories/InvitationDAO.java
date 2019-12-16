package it.unical.demacs.asde.signme.repositories;

import org.springframework.data.repository.CrudRepository;

import it.unical.demacs.asde.signme.models.Invitation;

public interface InvitationDAO extends CrudRepository<Invitation, Integer> {

}
