package it.unical.demacs.asde.signme.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer courseId;

	private String subject;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "followingCourses")
	@JsonIgnore
	private Set<User> students;

	@OneToMany(mappedBy = "course")
	@JsonIgnore
	private Set<Lecture> lectures;

	@OneToMany(mappedBy = "course")
	@JsonIgnore
	private Set<Notice> notices;

	@OneToMany(mappedBy = "course")
	private Set<Material> material;

	@ManyToOne
	@JsonIgnore
	private User lecturer;

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

	public Set<Notice> getNotices() {
		return notices;
	}

	public void setNotices(Set<Notice> notices) {
		this.notices = notices;
	}

//	public Set<Material> getMaterials() {
//		return materials;
//	}
//
//	public void setMaterials(Set<Material> materials) {
//		this.materials = materials;
//	}
//	

}
