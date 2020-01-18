package it.unical.demacs.asde.signme.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Notice")
public class Notice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer noticeId;
	@ManyToOne
	@JsonIgnore
	private Course course;
	private String description;
	

	public Notice() {
		super();
	}

	public Notice(Integer noticeId, String description, Course course) {
		super();
		this.noticeId = noticeId;
		this.description = description;
		this.course = course;
	}

	public Integer getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
