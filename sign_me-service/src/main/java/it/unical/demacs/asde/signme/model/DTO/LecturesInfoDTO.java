package it.unical.demacs.asde.signme.model.DTO;

import java.util.Set;

import it.unical.demacs.asde.signme.model.Lecture;

public class LecturesInfoDTO {

	Set<Lecture> lectures;
	String lecturer;

	public Set<Lecture> getLectures() {
		return lectures;
	}

	public void setLectures(Set<Lecture> lectures) {
		this.lectures = lectures;
	}

	public String getLecturer() {
		return lecturer;
	}

	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}

}
