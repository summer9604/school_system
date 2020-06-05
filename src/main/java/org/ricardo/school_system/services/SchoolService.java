package org.ricardo.school_system.services;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.ClassDao;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Class;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SchoolService {

	@Autowired
	private SchoolDao schoolDao;

	@Autowired
	private ClassDao classDao;

	@Autowired
	private TeacherDao teacherDao;

	@Autowired
	private JwtHandler jwtHandler;

	public School addSchool(School school) {		
		return schoolDao.add(school);
	}

	@Transactional
	public ResponseEntity<?> getAllSchools() {
		return new ResponseEntity<>(schoolDao.getAll(), HttpStatus.OK);
	}

	@Transactional
	public School getSchoolById(int id) {
		return schoolDao.getById(id);
	}

	@Transactional
	public School getSchoolByEmail(String email) {
		return schoolDao.getByEmail(email);
	}

	@Transactional
	public void deleteSchool(int id) {
		schoolDao.delete(id);
	}

	@Transactional
	public School updateSchool(School school) {
		return schoolDao.update(school);
	}

	public Class addClass(Class schoolClass) {		
		return classDao.add(schoolClass);
	}

	@Transactional
	public List<Class> getAllClasses() {
		return classDao.getAll();
	}

	@Transactional
	public ResponseEntity<?> getClassById(HttpServletRequest request, int id) {
		return new ResponseEntity<>(classDao.getById(id), HttpStatus.OK);
	}

	@Transactional
	public Class getClassByEmail(String email) {
		return classDao.getByEmail(email);
	}

	@Transactional
	public void deleteClass(int id) {
		classDao.delete(id);
	}

	@Transactional
	public Class updateClass(Class schoolClass) {
		return classDao.update(schoolClass);
	}

	@Transactional
	public ResponseEntity<?> getClassesByTeacherId(HttpServletRequest request) {

		JwtUserPermissions userPermissions = retrievePermissions(request);

		return new ResponseEntity<>(classDao.getClassesByTeacherId(userPermissions.getId()), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> hireTeacher(HttpServletRequest request, int teacherId) {

		JwtUserPermissions userPermissions = retrievePermissions(request);

		Teacher teacher = teacherDao.getById(teacherId);

		School school = schoolDao.getById(userPermissions.getSchoolId());

		teacher.setSchool(school);

		teacherDao.update(teacher);

		return new ResponseEntity<>("Teacher '" + teacher.getName() + "' is now teaching in '" + school.getName() + "'", HttpStatus.OK);
	}

	public ResponseEntity<?> getClassesBySchool(int id, HttpServletRequest request) {
		return new ResponseEntity<>(classDao.getClassesBySchoolId(id), HttpStatus.OK);
	}

	private JwtUserPermissions retrievePermissions(HttpServletRequest request) {

		for(Cookie cookie : request.getCookies()) {

			if(cookie.getName().equals("jwtToken"))
				return jwtHandler.getUserPermissions(cookie.getValue());
		}

		return null;
	}
	
}




















