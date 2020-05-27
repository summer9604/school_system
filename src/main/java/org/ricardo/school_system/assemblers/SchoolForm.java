package org.ricardo.school_system.assemblers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SchoolForm {

	private String name;
	
	@JsonCreator
	public SchoolForm(@JsonProperty("name") String name) {
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
