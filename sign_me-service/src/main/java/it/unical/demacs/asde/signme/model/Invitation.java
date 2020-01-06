package it.unical.demacs.asde.signme.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Invitation")
public class Invitation {

	@Id
	private String invitationId;
	private Integer course;
	private Boolean pending;
	@ManyToOne
	private User student;

	public Invitation() {
		super();
	}

	public Invitation(String invitationId, User student, Integer course, Boolean pending) {
		super();
		this.invitationId = invitationId;
		this.student = student;
		this.course = course;
		this.pending = pending;
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Boolean getPending() {
		return pending;
	}

	public void setPending(Boolean pending) {
		this.pending = pending;
	}

}
