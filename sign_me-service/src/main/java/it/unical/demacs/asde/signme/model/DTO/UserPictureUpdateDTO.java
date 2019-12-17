package it.unical.demacs.asde.signme.model.DTO;

import org.springframework.web.multipart.MultipartFile;

public class UserPictureUpdateDTO {
	
	private String email;
	private MultipartFile picture;
	
	public MultipartFile getPicture() {
		return picture;
	}
	
	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
