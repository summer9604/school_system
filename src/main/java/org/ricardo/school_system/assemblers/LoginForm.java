package org.ricardo.school_system.assemblers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginForm {

	private String email;
	private String password;
	
	public LoginForm() {
		
	}
	
	@JsonCreator
	public LoginForm(@JsonProperty("email") String email, 
					 @JsonProperty("password") String password) {
		this.email = email;
		this.password = password;
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
		return "LoginInfo [email=" + email + ", password=" + password + "]";
	}
	
}
