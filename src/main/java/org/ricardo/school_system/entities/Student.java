package org.ricardo.school_system.entities;

import java.time.LocalDate;
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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "student")
@JsonIgnoreProperties({"studentClass", "subjects"})
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idstudent")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "phonenumber")
	private int phonenumber;
	
	@Column(name = "password")
	private String password;
	
	@Column(name="email")
	private String email;
	
	@Column(name="student_role")
	private String studentRole;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, 
				           CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "class_id")
	private Class studentClass;
	//se eu elimino o aluno, todos os resultados intermediarios tambem sao eliminados...
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
	@JoinTable(name = "student_subject",
               joinColumns = @JoinColumn(name = "student_id"),
               inverseJoinColumns = @JoinColumn(name = "subject_id"))
	private List<Subject> subjects;

	@Column(name = "createdAt")
	private LocalDate createdAt;

	@Column(name = "updatedAt")
	private LocalDate updatedAt;

	public Student() {

	}

	public Student(String name, String address, int phonenumber, String email, String password) {
		this.name = name;
		this.address = address;
		this.phonenumber = phonenumber;
		this.email = email;
		this.password = password;
		this.studentRole = "ROLE_STUDENT";
		this.createdAt = LocalDate.now();
		this.updatedAt = LocalDate.now();
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
	
	public String getStudentRole() {
		return studentRole;
	}

	public void setStudentRole(String studentRole) {
		this.studentRole = studentRole;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public int getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(int phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Class getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(Class studentClass) {
		this.studentClass = studentClass;
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
		return "Student [id=" + id + ", name=" + name + ", address=" + address + ", phonenumber=" + phonenumber
				+ ", email=" + email + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
