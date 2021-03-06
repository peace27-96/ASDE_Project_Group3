package it.unical.demacs.asde.signme.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Lecture")
public class Lecture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer lectureId;
	private String description;
	@Temporal(TemporalType.DATE)
	private Date date;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "attendedLectures")
	@JsonIgnore
	private Set<User> students;

	@ManyToOne
	@JsonIgnore
	private Course course;

	public Lecture() {
		super();
	}

	public Lecture(Integer lectureId, String description, Course course, Set<User> students) {
		super();
		this.lectureId = lectureId;
		this.description = description;
		this.course = course;
		this.students = students;
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

	public Set<User> getStudents() {
		return students;
	}

	public void setStudents(Set<User> students) {
		this.students = students;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;
		if (!(o instanceof Lecture)) {
			return false;
		}

		Lecture lecture = (Lecture) o;

		return lecture.lectureId.equals(lectureId);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + lectureId.hashCode();
		return result;
	}

}
