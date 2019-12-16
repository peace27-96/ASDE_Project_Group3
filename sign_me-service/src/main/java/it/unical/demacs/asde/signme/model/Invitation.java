package it.unical.demacs.asde.signme.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Invitation")
public class Invitation {

	@Id
	private Integer invitationId;
	private String receiver;
	private String course;

	@ManyToOne
	private User lecturer;

	public Invitation() {
		super();
	}

	public Invitation(Integer invitationId, String receiver, String course, User lecturer) {
		super();
		this.invitationId = invitationId;
		this.receiver = receiver;
		this.course = course;
		this.lecturer = lecturer;
	}

	public Integer getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(Integer invitationId) {
		this.invitationId = invitationId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public User getLecturer() {
		return lecturer;
	}

	public void setLecturer(User lecturer) {
		this.lecturer = lecturer;
	}

}
