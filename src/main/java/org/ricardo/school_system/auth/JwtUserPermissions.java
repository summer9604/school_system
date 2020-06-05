package org.ricardo.school_system.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtUserPermissions {

	private int id;
	private String permissions;
	private int schoolId;

	@JsonCreator
	public JwtUserPermissions(@JsonProperty("id") int id,@JsonProperty("permissions") String permissions, 
							  @JsonProperty("schoolId") int schoolId) {
		this.id = id;
		this.permissions = permissions;
		this.schoolId = schoolId;
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
	
	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	@Override
	public String toString() {
		return "JwtUserPermissions [id=" + id + ", permissions=" + permissions + ", schoolId=" + schoolId + "]";
	}

}
