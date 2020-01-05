package it.unical.demacs.asde.signme.model.DTO;

public class ConfirmSubscriptionDTO {

	private String student;

	private Integer courseId;

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

}
