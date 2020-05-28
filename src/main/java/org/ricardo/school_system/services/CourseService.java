package org.ricardo.school_system.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.DegreeSubjectBundle;
import org.ricardo.school_system.daos.DegreeDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.entities.Degree;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CourseService{

	@Autowired
	private DegreeDao degreeDao;
	
	@Autowired
	private SubjectDao subjectDao;
	
	public ResponseEntity<?> addDegree(HttpServletRequest request, DegreeSubjectBundle degreeSubjectBundle) {	
		
		HttpSession session = request.getSession(false);
		
		if (session == null) throw new SessionNotFoundException("You are not logged in.");

		Degree degree = new Degree(degreeSubjectBundle.getDegreeName());

		for(int subjectId : degreeSubjectBundle.getSubjectIds()) {
			degree.addSubject(subjectDao.getById(subjectId)); //Exception - degree already exists.
		}

		return new ResponseEntity<>(degreeDao.add(degree), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAllDegrees(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		
		if (session == null) throw new SessionNotFoundException("You are not logged in.");
		
		String[] permissions = (String[]) session.getAttribute("user-permissions");
		
		for(String permission : permissions) {
			if(permission.equals("STUDENT")) 
				return new ResponseEntity<>(degreeDao.getAll(), HttpStatus.OK);
		}
			
		throw new OperationNotAuthorizedException("You dont´t have enough permissions.");
	}

	@Transactional
	public ResponseEntity<?> getDegreeById(HttpServletRequest request, int id) {
		
		HttpSession session = request.getSession(false);
		
		if (session == null) throw new SessionNotFoundException("You are not logged in.");

		return new ResponseEntity<>(degreeDao.getById(id), HttpStatus.OK);
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
	public ResponseEntity<?> getAllSubjects(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if (session == null) throw new SessionNotFoundException("You are not logged in.");

		return new ResponseEntity<>(subjectDao.getAll(), HttpStatus.OK);		
	}

	@Transactional
	public ResponseEntity<?> getSubjectById(HttpServletRequest request, int id) {
		
		HttpSession session = request.getSession(false);
		
		if (session == null) throw new SessionNotFoundException("You are not logged in.");

		return new ResponseEntity<>(subjectDao.getById(id), HttpStatus.OK);
	}

	@Transactional
	public void deleteSubject(int id) {
		subjectDao.delete(id);
	}

	@Transactional
	public Subject updateSubject(Subject subject) {
		return subjectDao.update(subject);
	}
	
}
