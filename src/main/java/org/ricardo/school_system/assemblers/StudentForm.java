package org.ricardo.school_system.assemblers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentForm {

	private String name;
	private String address;
	private int phonenumber;
	private String email;
	private String password;
	
	@JsonCreator
	public StudentForm(@JsonProperty("name") String name, @JsonProperty("address") String address,
				       @JsonProperty("phonenumber") int phonenumber, @JsonProperty("email") String email,
				       @JsonProperty("password") String password) {
		this.name = name;
		this.address = address;
		this.phonenumber = phonenumber;
		this.email = email;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "StudentForm [name=" + name + ", address=" + address + ", phonenumber=" + phonenumber + ", email="
				+ email + ", password=" + password + "]";
	}
	
}
