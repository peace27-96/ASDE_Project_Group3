package it.unical.demacs.asde.signme.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {

	@Id
	private String email;
	private String password;
	private String firstName;
	private String lastName;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // lazy load
	@JoinTable(name = "Subscriptions", joinColumns = { @JoinColumn(name = "email") }, inverseJoinColumns = {
			@JoinColumn(name = "courseId") })
	private List<Course> followingCourses;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "Attendances", joinColumns = { @JoinColumn(name = "email") }, inverseJoinColumns = {
			@JoinColumn(name = "lectureId") })
	private List<Lecture> attendedLectures;

	public User(String email, String password, String first_name, String lastName, List<Course> followingCourses,
			List<Lecture> attendedLectures) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = first_name;
		this.lastName = lastName;
		this.followingCourses = followingCourses;
		this.attendedLectures = attendedLectures;
	}

	public User() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Course> getFollowingCourses() {
		return followingCourses;
	}

	public void setFollowingCourses(List<Course> followingCourses) {
		this.followingCourses = followingCourses;
	}

	public List<Lecture> getAttendedLectures() {
		return attendedLectures;
	}

	public void setAttendedLectures(List<Lecture> attendedLectures) {
		this.attendedLectures = attendedLectures;
	}

}
