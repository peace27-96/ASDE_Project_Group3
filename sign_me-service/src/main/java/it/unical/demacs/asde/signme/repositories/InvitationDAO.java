package it.unical.demacs.asde.signme.repositories;

import org.springframework.data.repository.CrudRepository;

import it.unical.demacs.asde.signme.model.Subscription;

public interface InvitationDAO extends CrudRepository<Subscription, Integer> {

}
