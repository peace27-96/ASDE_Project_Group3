package it.unical.demacs.asde.signme.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Lecture")
public class Lecture {

	@Id
	private Integer lectureId;
	private String description;
	@Temporal(TemporalType.DATE)
	private Date date;

	@ManyToMany(mappedBy = "attendedLectures")
	private Set<User> followingCourses;

	@ManyToOne
	private Course course;

	public Lecture() {
		super();
	}

	public Lecture(Integer lectureId, String description, Course course) {
		super();
		this.lectureId = lectureId;
		this.description = description;
		this.course = course;
	}

	public Integer getLectureId() {
		return lectureId;
	}

	public void setLectureId(Integer lectureId) {
		this.lectureId = lectureId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set<User> getFollowingCourses() {
		return followingCourses;
	}

	public void setFollowingCourses(Set<User> followingCourses) {
		this.followingCourses = followingCourses;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
