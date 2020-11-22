package org.ricardo.school_system.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentGradeForm {

	private int studentId;
	private int grade;
	
	public StudentGradeForm() {
		
	}
	
	@JsonCreator
	public StudentGradeForm(@JsonProperty("studentId") int studentId, @JsonProperty("grade") int grade) {
		this.studentId = studentId;
		this.grade = grade;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "StudentGradeForm [studentId=" + studentId + ", grade=" + grade + "]";
	}
}
