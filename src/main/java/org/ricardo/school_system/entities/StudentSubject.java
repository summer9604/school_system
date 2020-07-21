package org.ricardo.school_system.entities;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="student_subject")
public class StudentSubject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idstudent_subject")
	private int id;

	@ManyToOne
	@JoinColumn(name="student_id")
	private Student student;

	@ManyToOne
	@JoinColumn(name="subject_id")
	private Subject subject;

	@Column(name="grade")
	private Integer grade;

	@Column(name="createdAt")
	private LocalDate createdAt;

	@Column(name="updatedAt")
	private LocalDate updatedAt;

	public StudentSubject() {

	}

	public StudentSubject(Student student, Subject subject) {
		this.student = student;
		this.subject = subject;
		this.createdAt = LocalDate.now();
		this.updatedAt = LocalDate.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
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
		return "StudentSubject [id=" + id + ", student=" + student + ", subject=" + subject + ", grade=" + grade + ","
				+ " createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
