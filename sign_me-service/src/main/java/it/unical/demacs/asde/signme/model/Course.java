package it.unical.demacs.asde.signme.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer courseId;
	
	private String subject;
	
	@ManyToOne
	private User lecturer;

	@ManyToMany(mappedBy = "followingCourses")
	Set<User> students;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
	private Set<Lecture> lectures;

	public Course() {
		super();
	}

	public Course(Integer courseId, String subject, Set<User> students, Set<Lecture> lectures, User lecturer) {
		super();
		this.courseId = courseId;
		this.subject = subject;
		this.students = students;
		this.lectures = lectures;
		this.lecturer = lecturer;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
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

	public Set<Lecture> getLectures() {
		return lectures;
	}

	public void setLectures(Set<Lecture> lectures) {
		this.lectures = lectures;
	}
	
	public User getLecturer() {
		return lecturer;
	}
	
	public void setLecturer(User lecturer) {
		this.lecturer = lecturer;
	}
	

}
