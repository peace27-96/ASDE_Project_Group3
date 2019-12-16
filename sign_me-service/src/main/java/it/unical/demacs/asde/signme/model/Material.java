package it.unical.demacs.asde.signme.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Material")
public class Material {

	@Id
	private Integer materialId;
	private String description;

	public Material() {
		super();
	}

	public Material(Integer materialId, String description) {
		super();
		this.materialId = materialId;
		this.description = description;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
