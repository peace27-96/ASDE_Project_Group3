package it.unical.demacs.asde.signme.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Lecture")
public class Lecture {

	@Id
	private Integer lectureId;
	private String description;

	public Lecture() {
		super();
	}

	public Lecture(Integer lectureId, String description) {
		super();
		this.lectureId = lectureId;
		this.description = description;
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

}
