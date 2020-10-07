package org.ricardo.school_system.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EditableUserProfileForm {

	private String address;
	private int phonenumber;
	private String email;
	
	@JsonCreator
	public EditableUserProfileForm(@JsonProperty("address") String address, @JsonProperty("phonenumber") int phonenumber, 
									  @JsonProperty("email") String email) {
		this.address = address;
		this.phonenumber = phonenumber;
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(int phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "EditableTeacherProfileForm [address=" + address + ", phonenumber=" + phonenumber + ", email=" + email
				+ "]";
	}
	
}
