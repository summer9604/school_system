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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "subject")
public class Subject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idsubject")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY,
        	    cascade = {CascadeType.DETACH, CascadeType.MERGE,
        			       CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "degree-subject",
               joinColumns = @JoinColumn(name = "subject_id"),
               inverseJoinColumns = @JoinColumn(name = "degree_id"))
	private List<Degree> degrees;
	
	@ManyToMany(fetch = FetchType.LAZY,
	    		cascade = { CascadeType.DETACH, CascadeType.MERGE,
			            	CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "student_subject",
           	   joinColumns = @JoinColumn(name = "subject_id"),
               inverseJoinColumns = @JoinColumn(name = "student_id"))
	private List<Student> students;
	
	@OneToMany(mappedBy = "subject",
	           cascade = {CascadeType.DETACH, CascadeType.MERGE,
	                      CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Teacher> teachers;
	
	@Column(name = "createdAt")
	private LocalDate createdAt;
	
	@Column(name = "updatedAt")
	private LocalDate updatedAt;

	public Subject() {
		
	}
	
	public Subject(String name) {
		this.name = name;
	}
	
	public void addDegree(Degree degree) {
		
		if (degrees == null) degrees = new LinkedList<>();
		
		degrees.add(degree);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public List<Degree> getDegrees() {
		return degrees;
	}

	public void setDegrees(List<Degree> degrees) {
		this.degrees = degrees;
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
		return "Subject [id=" + id + ", name=" + name + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
