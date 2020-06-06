package org.ricardo.school_system.assemblers;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TeacherClassForm {

	private int teachedId;
	private List<Integer> classesId;

	@JsonCreator
	public TeacherClassForm(@JsonProperty("teacherId") int teacherId, @JsonProperty("classesId") List<Integer> classesId) {
		this.teachedId = teacherId;
		this.classesId = classesId;
	}

	public int getTeachedId() {
		return teachedId;
	}

	public void setTeachedId(int teachedId) {
		this.teachedId = teachedId;
	}

	public List<Integer> getClassesId() {
		return classesId;
	}

	public void setClassesId(List<Integer> classesId) {
		this.classesId = classesId;
	}

	@Override
	public String toString() {
		return "TeacherClassForm [teachedId=" + teachedId + ", classesId=" + classesId + "]";
	}

}
