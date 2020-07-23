package org.ricardo.school_system.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GreetingsHolder {

	private String greet;
	private int userId;
	private String userRole;
	
	@JsonCreator
	public GreetingsHolder(@JsonProperty("greet") String greet, 
			   			   @JsonProperty("userId") int userId, 
			   			   @JsonProperty("userRole") String userRole) {
		this.greet = greet;
		this.userId = userId;
		this.userRole = userRole;
	}

	public String getGreet() {
		return greet;
	}

	public void setGreet(String greet) {
		this.greet = greet;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		return "GreetingsHolder [greet=" + greet + ", userId=" + userId + ", userRole=" + userRole + "]";
	}
	
}
