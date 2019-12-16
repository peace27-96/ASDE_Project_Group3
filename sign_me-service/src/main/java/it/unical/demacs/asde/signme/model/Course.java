package it.unical.demacs.asde.signme.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Course")
public class Course {

	@Id
	private Integer courseId;
	private String subject;
	
	@ManyToMany(mappedBy = "followingCourses")
	Set<User> students;

	public Course() {
		super();
	}

	public Course(int courseId, String subject, Set<User> students) {
		super();
		this.courseId = courseId;
		this.subject = subject;
		this.students = students;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
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
