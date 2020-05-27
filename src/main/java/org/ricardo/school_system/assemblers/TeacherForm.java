package org.ricardo.school_system.assemblers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TeacherForm {

	private String name;
	private String address;
	private int phonenumber;
	private String email;
	private String password;
	private int subjectId;
	
	@JsonCreator
	public TeacherForm(@JsonProperty("name") String name, @JsonProperty("address") String address, 
					   @JsonProperty("phonenumber") int phonenumber, @JsonProperty("email") String email, 
					   @JsonProperty("password") String password, @JsonProperty("subjectId") int subjectId) {
		this.name = name;
		this.address = address;
		this.phonenumber = phonenumber;
		this.email = email;
		this.password = password;
		this.subjectId = subjectId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public String toString() {
		return "TeacherInfo [name=" + name + ", address=" + address + ", phonenumber=" + phonenumber + ", email="
				+ email + ", subjectId=" + subjectId + "]";
	}
	
}
