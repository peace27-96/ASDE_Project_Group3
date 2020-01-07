package it.unical.demacs.asde.signme.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Users")
public class User {

	@Id
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String profilePicture;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "subscriptions", joinColumns = { @JoinColumn(name = "email") }, inverseJoinColumns = {
			@JoinColumn(name = "courseId") })
	private Set<Course> followingCourses;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "attendances", joinColumns = { @JoinColumn(name = "email") }, inverseJoinColumns = {
			@JoinColumn(name = "lectureId") })
	@JsonIgnore
	private Set<Lecture> attendedLectures;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "lecturer")
	private Set<Course> createdCourses;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "student")
	@JsonIgnore
	private List<Invitation> userInvitations;

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

	public List<Invitation> getUserInvitations() {
		return userInvitations;
	}

	public void setUserInvitations(List<Invitation> userInvitations) {
		this.userInvitations = userInvitations;
	}

	@Override
	public String toString() {
		return this.email + " " + this.password + " " + this.firstName + " " + this.lastName + " " + this.profilePicture
				+ " " + this.createdCourses.size() + " " + this.followingCourses.size() + " "
				+ this.attendedLectures.size();
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;
		if (!(o instanceof User)) {
			return false;
		}

		User user = (User) o;

		return user.email.equals(email);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + email.hashCode();
		return result;
	}
}
