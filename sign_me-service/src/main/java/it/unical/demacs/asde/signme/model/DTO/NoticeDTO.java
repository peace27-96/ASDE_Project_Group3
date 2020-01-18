package it.unical.demacs.asde.signme.model.DTO;

public class NoticeDTO {
	
	private String noticeDescription;
	private Integer courseId;

	public String getNoticeDescription() {
		return noticeDescription;
	}

	public void setNoticeDescription(String noticeDescription) {
		this.noticeDescription = noticeDescription;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	
}
