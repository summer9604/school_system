package org.ricardo.school_system.entities;

import java.sql.Date;
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

@Entity
@Table(name = "class")
public class Class {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "school_year")
	private int schoolYear;

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
	private Date createdAt;

	@Column(name="updatedAt")
	private Date updatedAt;

	public Class() {
		
	}
	
	public Class(int schoolYear) {
		this.schoolYear = schoolYear;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
