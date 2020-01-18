package it.unical.demacs.asde.signme.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Material")
public class Material {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer materialId;
	private String materialPath;
	private String description;
	@ManyToOne
	@JsonIgnore
	private Course course;

	public Material() {
		super();
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public String getMaterialPath() {
		return materialPath;
	}

	public void setMaterialPath(String materialPath) {
		this.materialPath = materialPath;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
