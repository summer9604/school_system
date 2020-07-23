package org.ricardo.school_system.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationStudentForm {

	private String name;
	private String address;
	private int phonenumber;
	private String email;
	private int classId;
	private String password;
	
	public RegistrationStudentForm() {
		
	}
	
	@JsonCreator
	public RegistrationStudentForm(@JsonProperty("name") String name, @JsonProperty("address") String address,
				       @JsonProperty("phonenumber") int phonenumber, @JsonProperty("email") String email,
				       @JsonProperty("classId") int classId, @JsonProperty("password") String password) {
		this.name = name;
		this.address = address;
		this.phonenumber = phonenumber;
		this.email = email;
		this.classId = classId;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegistrationStudentForm [name=" + name + ", address=" + address + ", phonenumber=" + phonenumber
				+ ", email=" + email + ", classId=" + classId + ", password=" + password + "]";
	}
	
}
