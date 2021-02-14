package org.ricardo.school_system.services;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.daos.ClassDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.dto.EditableUserProfileForm;
import org.ricardo.school_system.dto.JwtUserPermissions;
import org.ricardo.school_system.dto.RegistrationTeacherForm;
import org.ricardo.school_system.dto.TeacherClassForm;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.TeacherNotFoundException;
import org.ricardo.school_system.entities.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

	@Autowired
	private TeacherDao teacherDao;

	@Autowired
	private SubjectDao subjectDao;

	@Autowired
	private ClassDao classDao;

	@Autowired
	private JwtHandler jwtHandler;

	@Transactional
	public ResponseEntity<?> add(HttpServletRequest request, RegistrationTeacherForm teacherInfo) {	

		Subject subject = subjectDao.getById(teacherInfo.getSubjectId());

		Teacher teacher = new Teacher();

		teacher.setName(teacherInfo.getName());
		teacher.setAddress(teacherInfo.getAddress());
		teacher.setPhoneNumber(teacherInfo.getPhoneNumber());
		teacher.setEmail(teacherInfo.getEmail());
		teacher.setPassword(teacherInfo.getPassword());
		teacher.setSubject(subject);

		teacherDao.add(teacher);

		return new ResponseEntity<>("Teacher '" + teacher.getName() + "' added to the School System.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {

		JwtUserPermissions userPermissions = retrievePermissions(request);

		List<Teacher> teachers;

		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN")) {

			teachers = teacherDao.getTeachersBySchoolId(userPermissions.getSchoolId());

			if (teachers.isEmpty())
				throw new TeacherNotFoundException("No teachers were found at school with id " + userPermissions.getSchoolId());

			return new ResponseEntity<>(teachers, HttpStatus.OK);
		}

		teachers = teacherDao.getAll();

		if (teachers.isEmpty())
			throw new TeacherNotFoundException("No teachers were found.");

		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getById(HttpServletRequest request) {		

		JwtUserPermissions userPermissions = retrievePermissions(request);

		return new ResponseEntity<>(teacherDao.getById(userPermissions.getId()), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getByIdForAdmin(int id, HttpServletRequest request) {	
		
		Teacher teacher = teacherDao.getById(id);
		
		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with id " + id + " not found.");
		
		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getByEmail(HttpServletRequest request, String email) {	
		
		Teacher teacher = teacherDao.getByEmail(email);
		
		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with email " + email + " not found.");
		
		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getByName(HttpServletRequest request, String name) {	
		
		List<Teacher> teachers = teacherDao.getByName(name);
		
		if (teachers.isEmpty())
			throw new TeacherNotFoundException("No teachers named " + name + " were found.");
		
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> delete(HttpServletRequest request, int id) {

		teacherDao.delete(id);

		return new ResponseEntity<>("Teacher with id " + id + " removed.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> update(int teacherId, EditableUserProfileForm editableUserProfileForm, HttpServletRequest request) {		
		
		Teacher teacher = teacherDao.getById(teacherId);
			
		teacher.setAddress(editableUserProfileForm.getAddress());
		teacher.setEmail(editableUserProfileForm.getEmail());
		teacher.setPhoneNumber(editableUserProfileForm.getPhonenumber());
			
		teacherDao.update(teacher);
		
		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> placeIntoClasses(TeacherClassForm teacherClassForm, HttpServletRequest request) {

		List<Integer> classesId = teacherClassForm.getClassesId();
		Teacher teacher = teacherDao.getById(teacherClassForm.getTeachedId());

		for(int classId : classesId) {
			Class schoolClass = classDao.getById(classId);
			teacher.addClass(schoolClass);
		}

		teacherDao.update(teacher);

		return new ResponseEntity<>("Teacher allocated in " + classesId.size() + " new classes.", HttpStatus.OK);
	}
	
	private JwtUserPermissions retrievePermissions(HttpServletRequest request) {
		return request.getHeader("jwttoken") != null ? jwtHandler.getUserPermissions(request.getHeader("jwttoken")) : null;
	}
}
