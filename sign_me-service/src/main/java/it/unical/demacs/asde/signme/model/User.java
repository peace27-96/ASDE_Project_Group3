package it.unical.demacs.asde.signme.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {

	@Id
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String profilePicture;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lecturer", fetch = FetchType.EAGER)
	private Set<Course> createdCourses;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // lazy load
	@JoinTable(name = "Subscriptions", joinColumns = { @JoinColumn(name = "email") }, inverseJoinColumns = {
			@JoinColumn(name = "courseId") })
	private Set<Course> followingCourses;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "Attendances", joinColumns = { @JoinColumn(name = "email") }, inverseJoinColumns = {
			@JoinColumn(name = "lectureId") })
	private Set<Lecture> attendedLectures;

	public User() {
		super();
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
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

	public Set<Course> getFollowingCourses() {
		return followingCourses;
	}

	public void setFollowingCourses(Set<Course> followingCourses) {
		this.followingCourses = followingCourses;
	}

	public Set<Lecture> getAttendedLectures() {
		return attendedLectures;
	}

	public void setAttendedLectures(Set<Lecture> attendedLectures) {
		this.attendedLectures = attendedLectures;
	}

	public void setCreatedCourses(Set<Course> createdCourses) {
		this.createdCourses = createdCourses;
	}

	public Set<Course> getCreatedCourses() {
		return createdCourses;
	}

}
