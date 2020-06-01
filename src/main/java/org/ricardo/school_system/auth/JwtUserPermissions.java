package org.ricardo.school_system.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtUserPermissions {

	private int id;
	private String permissions;

	@JsonCreator
	public JwtUserPermissions(@JsonProperty("id") int id,@JsonProperty("permissions") String permissions) {
		this.id = id;
		this.permissions = permissions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "JwtUserPermissions [id=" + id + ", permissions=" + permissions + "]";
	}

}
