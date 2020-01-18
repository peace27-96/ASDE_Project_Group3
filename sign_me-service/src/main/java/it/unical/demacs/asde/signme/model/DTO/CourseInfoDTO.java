package it.unical.demacs.asde.signme.model.DTO;

import java.util.Set;

import it.unical.demacs.asde.signme.model.Material;
import it.unical.demacs.asde.signme.model.Notice;
import it.unical.demacs.asde.signme.model.User;

public class CourseInfoDTO {

	private LecturesInfoDTO lecturesInfoDTO;
	private Set<User> users;
	private Set<Notice> notices;
	private Set<Material> material;

	public LecturesInfoDTO getLecturesInfoDTO() {
		return lecturesInfoDTO;
	}

	public void setLecturesInfoDTO(LecturesInfoDTO lecturesInfoDTO) {
		this.lecturesInfoDTO = lecturesInfoDTO;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Notice> getNotices() {
		return notices;
	}

	public void setNotices(Set<Notice> notices) {
		this.notices = notices;
	}

	public Set<Material> getMaterial() {
		return material;
	}

	public void setMaterial(Set<Material> material) {
		this.material = material;
	}

}
