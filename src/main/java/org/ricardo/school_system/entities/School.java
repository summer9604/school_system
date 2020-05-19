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
@Table(name="school")
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idschool")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phonenumber")
	private int phonenumber;
	
	@OneToMany(mappedBy = "school",
	           cascade = {CascadeType.DETACH, CascadeType.MERGE,
	                      CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Teacher> teachers;
	
	@OneToMany(mappedBy = "school",
	           cascade = {CascadeType.DETACH, CascadeType.MERGE,
	                      CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Class> classes;
	
	@ManyToMany(fetch = FetchType.LAZY,
        		cascade = {CascadeType.DETACH, CascadeType.MERGE,
        				   CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "school_degree",
           	   joinColumns = @JoinColumn(name = "school_id"),
               inverseJoinColumns = @JoinColumn(name = "degree_id"))
	private List<Degree> degrees;
	
	@Column(name="createdAt")
	private LocalDate createdAt;

	@Column(name="updatedAt")
	private LocalDate updatedAt;

	public School() {
		
	}
	
	public School(String name, String address, int phonenumber) {
		this.name = name;
		this.address = address;
		this.phonenumber = phonenumber;
	}
	
	public void addClass(Class newClass, Degree degree) {
		
		if (classes == null) classes = new LinkedList<>();
		
		classes.add(newClass);
		newClass.setSchool(this);
		newClass.setDegree(degree);
	}
	
	public void addTeacher(Teacher teacher) {
		
		if (teachers == null) teachers = new LinkedList<>();
		
		teachers.add(teacher);
		teacher.setSchool(this);
	}
	
	public void addDegree(Degree degree) {
		
		if (degrees == null) degrees = new LinkedList<>();
		
		degrees.add(degree);
		degree.addSchool(this);
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(int phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
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
		return "School [id=" + id + ", name=" + name + ", address=" + address + ", phonenumber=" + phonenumber
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
