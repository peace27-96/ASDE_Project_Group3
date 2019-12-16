package it.unical.demacs.asde.signme.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Course")
public class Course {

	@Id
	private Integer courseId;
	private String subject;

	@ManyToMany(mappedBy = "followingCourses")
	Set<User> students;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
	private List<Lecture> lectures;

	public Course() {
		super();
	}

	public Course(int courseId, String subject, Set<User> students, List<Lecture> lectures) {
		super();
		this.courseId = courseId;
		this.subject = subject;
		this.students = students;
		this.lectures = lectures;
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

	public List<Lecture> getLectures() {
		return lectures;
	}

	public void setLectures(List<Lecture> lectures) {
		this.lectures = lectures;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

}
