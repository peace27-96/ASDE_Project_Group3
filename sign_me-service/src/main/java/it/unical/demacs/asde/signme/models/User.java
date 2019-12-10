package it.unical.demacs.asde.signme.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
	private String first_name;
	private String second_name;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable( name = "Subscriptions", 
				joinColumns = {@JoinColumn(name = "email")},
				inverseJoinColumns = {@JoinColumn(name = "course_id")})
	private Set<Course> courses;

	public User(String email, String password, String first_name, String second_name) {
		super();
		this.email = email;
		this.password = password;
		this.first_name = first_name;
		this.second_name = second_name;
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

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getSecond_name() {
		return second_name;
	}

	public void setSecond_name(String second_name) {
		this.second_name = second_name;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

}
