package org.ricardo.school_system.services;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.DegreeSubjectBundle;
import org.ricardo.school_system.daos.DegreeDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.entities.Degree;
import org.ricardo.school_system.entities.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

	@Autowired
	private DegreeDao degreeDao;

	@Autowired
	private SubjectDao subjectDao;

	public ResponseEntity<?> addDegree(HttpServletRequest request, DegreeSubjectBundle degreeSubjectBundle) {

		Degree degree = new Degree(degreeSubjectBundle.getDegreeName());

		for (int subjectId : degreeSubjectBundle.getSubjectIds()) {
			degree.addSubject(subjectDao.getById(subjectId)); // Exception - degree already exists.
		}

		return new ResponseEntity<>(degreeDao.add(degree), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAllDegrees(HttpServletRequest request) {
		return new ResponseEntity<>(degreeDao.getAll(), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getDegreeById(HttpServletRequest request, int id) {
		return new ResponseEntity<>(degreeDao.getById(id), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> deleteDegree(int id) {

		degreeDao.delete(id);

		return new ResponseEntity<>("Degree with id " + id + " deleted.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getDegreesBySchool(int id, HttpServletRequest request) {
		return new ResponseEntity<>(degreeDao.getDegreesBySchoolId(id), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> updateDegree(Degree degree) {
		return new ResponseEntity<>(degreeDao.update(degree), HttpStatus.OK);
	}

	public ResponseEntity<?> addSubject(String subjectName) {
		return new ResponseEntity<>(subjectDao.add(new Subject(subjectName)), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAllSubjects(HttpServletRequest request) {
		return new ResponseEntity<>(subjectDao.getAll(), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getSubjectById(HttpServletRequest request, int id) {
		return new ResponseEntity<>(subjectDao.getById(id), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> deleteSubject(int id) {

		subjectDao.delete(id);

		return new ResponseEntity<>("Subject with id " + id + " deleted.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> updateSubject(Subject subject) {
		return new ResponseEntity<>(subjectDao.update(subject), HttpStatus.OK);
	}

}
