package org.ricardo.school_system.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TeacherClassForm {

	private int teacherId;
	private List<Integer> classesId;

	public TeacherClassForm() {
		
	}
	
	@JsonCreator
	public TeacherClassForm(@JsonProperty("teacherId") int teacherId, @JsonProperty("classesId") List<Integer> classesId) {
		this.teacherId = teacherId;
		this.classesId = classesId;
	}

	public int getTeachedId() {
		return teacherId;
	}

	public void setTeachedId(int teacherId) {
		this.teacherId = teacherId;
	}

	public List<Integer> getClassesId() {
		return classesId;
	}

	public void setClassesId(List<Integer> classesId) {
		this.classesId = classesId;
	}

	@Override
	public String toString() {
		return "TeacherClassForm [teacherId=" + teacherId + ", classesId=" + classesId + "]";
	}

}
