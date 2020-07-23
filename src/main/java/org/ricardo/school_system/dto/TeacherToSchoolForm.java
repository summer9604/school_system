package org.ricardo.school_system.dto;

public class TeacherToSchoolForm { //RISCO DE SER APAGADO!

	private String teacherName;
	private String schoolName;
	
	public TeacherToSchoolForm() {
		
	}

	public TeacherToSchoolForm(String teacherName, String schoolName) {
		this.teacherName = teacherName;
		this.schoolName = schoolName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	@Override
	public String toString() {
		return "TeacherSchool [teacherName=" + teacherName + ", schoolName=" + schoolName + "]";
	}
	
}
