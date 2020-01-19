package it.unical.demacs.asde.signme.model.DTO;

public class AttendanceAccuracyDTO {

	private String email;
	private String firstName;
	private String lastName;
	private int accuracy;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;
		if (!(o instanceof AttendanceAccuracyDTO)) {
			return false;
		}

		AttendanceAccuracyDTO user = (AttendanceAccuracyDTO) o;

		return user.email.equals(email);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + email.hashCode();
		return result;
	}

}
