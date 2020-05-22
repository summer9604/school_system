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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "degree")
@JsonIgnoreProperties({"classes", "schools", "subjects"})
public class Degree {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="idDegree")
	private int id;

	@Column(name ="name")
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY,
            	cascade = {CascadeType.DETACH, CascadeType.MERGE,
            			   CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "degree_subject",
               joinColumns = @JoinColumn(name = "degree_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
	private List<Subject> subjects;
	
	@ManyToMany(fetch = FetchType.LAZY,
        		cascade = {CascadeType.DETACH, CascadeType.MERGE,
        			       CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "school_degree",
           	   joinColumns = @JoinColumn(name = "degree_id"),
               inverseJoinColumns = @JoinColumn(name = "school_id"))
	private List<School> schools;
	
	@OneToMany(mappedBy = "degree",
	           cascade = {CascadeType.DETACH, CascadeType.MERGE,
	                      CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Class> classes;

	@Column(name ="createdAt")
	private LocalDate createdAt;

	@Column(name ="updatedAt")
	private LocalDate updatedAt;

	public Degree() {

	}

	public Degree(String name) {
		this.name = name;
		this.createdAt = LocalDate.now();
		this.updatedAt = LocalDate.now();
	}
	
	public void addSubject(Subject subject) {
		
		if (subjects == null) subjects = new LinkedList<>();
		
		subjects.add(subject);
	}
	
	public void addSchool(School school) {
		
		if (schools == null) schools = new LinkedList<>();
		
		schools.add(school);
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

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<School> getSchools() {
		return schools;
	}

	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
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
		return "Degree [id=" + id + ", name=" + name + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
