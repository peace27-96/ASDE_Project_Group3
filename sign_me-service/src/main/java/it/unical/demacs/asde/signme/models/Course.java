package it.unical.demacs.asde.signme.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Course")
public class Course {

	@Id
	private int course_id;
	private String subject;

	@ManyToMany(mappedBy="courses")
	Set<User> students;
	
	public Course() {
		super();
	}

	public Course(int course_id, String subject, Set<User> students) {
		super();
		this.course_id = course_id;
		this.subject = subject;
		this.students = students;
	}

	public int getCourse_id() {
		return course_id;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Set<User> getStudents() {
		return students;
	}

	public void setStudents(Set<User> students) {
		this.students = students;
	}

}
