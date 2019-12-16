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
	private String student;
	private String lecturer;
	private Integer course;

	public Invitation() {
		super();
	}

	public Invitation(Integer invitationId, String receiver, Integer course, String lecturer) {
		super();
		this.invitationId = invitationId;
		this.lecturer = receiver;
		this.course = course;
		this.lecturer = lecturer;
	}

	public Integer getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(Integer invitationId) {
		this.invitationId = invitationId;
	}

	public String getLecturer() {
		return lecturer;
	}

	public void setLecturer(String receiver) {
		this.lecturer = receiver;
	}

	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

}
