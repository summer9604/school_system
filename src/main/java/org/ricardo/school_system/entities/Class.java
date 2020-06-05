package org.ricardo.school_system.entities;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "class")
@JsonIgnoreProperties({"school", "degree", "students", "teachers"})
public class Class {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idClass")
	private int id;
	
	@Column(name = "school_year")
	private int schoolYear;

	@Column(name="email")
	private String email;
	
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, 
	           		       CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "school_id")
	private School school;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, 
		       	           CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "degree_id")
	private Degree degree;

	@OneToMany(mappedBy = "studentClass", 
			   cascade = { CascadeType.DETACH, CascadeType.MERGE, 
					   	   CascadeType.PERSIST, CascadeType.REFRESH })
	private List<Student> students;
	
	@ManyToMany(fetch = FetchType.LAZY,
	            cascade = { CascadeType.DETACH, CascadeType.MERGE,
	        		        CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "teacher_class",
       	       joinColumns = @JoinColumn(name = "class_id"),
               inverseJoinColumns = @JoinColumn(name = "teacher_id"))
	private List<Teacher> teachers;

	@Column(name="createdAt")
	private LocalDate createdAt;

	@Column(name="updatedAt")
	private LocalDate updatedAt;

	public Class() {
		
	}
	
	public Class(int schoolYear, String email) {
		this.schoolYear = schoolYear;
		this.email = email;
		this.createdAt = LocalDate.now();
		this.updatedAt = LocalDate.now();
	}

	public void addStudent(Student student) {
		
		if (students == null) students = new LinkedList<>();
		
		students.add(student);
		student.setStudentClass(this);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(int schoolYear) {
		this.schoolYear = schoolYear;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Class [id=" + id + ", schoolYear=" + schoolYear + ", email=" + email + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
}
