package org.ricardo.school_system.services;

import java.util.List;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.DegreeSubjectBundle;
import org.ricardo.school_system.daos.DegreeDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.entities.Degree;
import org.ricardo.school_system.entities.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService{

	@Autowired
	private DegreeDao degreeDao;
	
	@Autowired
	private SubjectDao subjectDao;
	
	public Degree addDegree(Degree degree) {	
		return degreeDao.add(degree);
	}

	@Transactional
	public List<Degree> getAllDegrees() {
		return degreeDao.getAll();
	}

	@Transactional
	public Degree getDegreeById(int id) {
		return degreeDao.getById(id);
	}

	@Transactional
	public void deleteDegree(int id) {
		degreeDao.delete(id);
	}

	@Transactional
	public Degree updateDegree(Degree degree) {
		return degreeDao.update(degree);
	}
	
	public Subject addSubject(Subject subject) {	
		return subjectDao.add(subject);
	}
	
	@Transactional
	public List<Subject> getAllSubjects() {
		return subjectDao.getAll();
	}

	@Transactional
	public Subject getSubjectById(int id) {
		return subjectDao.getById(id);
	}

	@Transactional
	public void deleteSubject(int id) {
		subjectDao.delete(id);
	}

	@Transactional
	public Subject updateSubject(Subject subject) {
		return subjectDao.update(subject);
	}
	
	@Transactional
	public Degree addDegreeWithSubjects(DegreeSubjectBundle degreeSubjectBundle){

		Degree degree = new Degree(degreeSubjectBundle.getDegreeName());

		for(int subjectId : degreeSubjectBundle.getSubjectIds()) {
			degree.addSubject(subjectDao.getById(subjectId));
		}

		degreeDao.add(degree);
		
		return degree;
	}
}
