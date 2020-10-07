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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="teacher")
@JsonIgnoreProperties({"classes", "school", "subject"})
public class Teacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idteacher")
	private int id;

	@Column(name="name")
	private String name;

	@Column(name="address")
	private String address;

	@Column(name="phonenumber")
	private int phonenumber;

	@Column(name="email")
	private String email;

	@Column(name="password")
	private String password;

	@JsonProperty("role") //O postman estava a receber esta propriedade como "teacherRole", entao usei JsonProperty e agora aparecem os 2 xD
	@Column(name="teacher_role")
	private String role;

	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
			 	  	 	  CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "school_id")
	private School school;

	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
					      CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "subject_id")
	private Subject subject;

	@ManyToMany(fetch = FetchType.LAZY,
			    cascade = {CascadeType.DETACH, CascadeType.MERGE,
				     	   CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "teacher_class",
	joinColumns = @JoinColumn(name = "teacher_id"),
	inverseJoinColumns = @JoinColumn(name = "class_id"))
	private List<Class> classes;

	@Column(name="createdAt")
	private LocalDate createdAt;

	@Column(name="updatedAt")
	private LocalDate updatedAt;

	@Column(name="retiredAt")
	private LocalDate retiredAt;

	public Teacher() {

	}

	public Teacher(String name, String address, int phonenumber, String email, String password, Subject subject) {
		this.name = name;
		this.address = address;
		this.phonenumber = phonenumber;
		this.email = email;
		this.password = password;
		this.subject = subject;
		this.role = "ROLE_TEACHER";
		this.createdAt = LocalDate.now();
		this.updatedAt = LocalDate.now();
	}


	public void addClass(Class schoolClass) {

		if (classes == null) classes = new LinkedList<>();

		classes.add(schoolClass);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTeacherRole() {
		return role;
	}

	public void setTeacherRole(String teacherRole) {
		this.role = teacherRole;
	}

	public void setRetiredAt(LocalDate retiredAt) {
		this.retiredAt = retiredAt;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
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

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
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

	public LocalDate getRetiredAt() {
		return retiredAt;
	}

	@Override
	public String toString() {
		return "Teacher [id=" + id + ", name=" + name + ", address=" + address + ", phonenumber=" + phonenumber
				+ ", email=" + email + ", password=" + password + ", teacherRole=" + role + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + ", retiredAt=" + retiredAt + "]";
	}

}

