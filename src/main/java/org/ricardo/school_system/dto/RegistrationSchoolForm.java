package org.ricardo.school_system.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationSchoolForm {

	private String name;
	
	@JsonCreator
	public RegistrationSchoolForm(@JsonProperty("name") String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SchoolInfo [name=" + name + "]";
	}
	
}
