package org.ricardo.school_system.services;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.RegistrationLocalAdminForm;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.AdminDao;
import org.ricardo.school_system.daos.ClassDao;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Admin;
import org.ricardo.school_system.entities.Class;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.SchoolNotFoundException;
import org.ricardo.school_system.exceptions.AdminNotFoundException;
import org.ricardo.school_system.exceptions.ClassNotFoundException;
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
	private AdminDao adminDao;

	@Autowired
	private JwtHandler jwtHandler;

	public School addSchool(School school) {		
		return schoolDao.add(school);
	}

	@Transactional
	public ResponseEntity<?> getAllSchools() {

		List<School> schools = schoolDao.getAll();

		if (schools.isEmpty())
			throw new SchoolNotFoundException("No schools were found.");

		return new ResponseEntity<>(schools, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getSchoolById(int id) {

		School school = schoolDao.getById(id);

		if (school == null)
			throw new SchoolNotFoundException("School with id " + id + " not found.");

		return new ResponseEntity<>(school, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getSchoolByEmail(String email) {

		School school = schoolDao.getByEmail(email);

		if (school == null)
			throw new SchoolNotFoundException("School with email " + email + " not found.");

		return new ResponseEntity<>(school, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> deleteSchool(int id) {

		schoolDao.delete(id);

		return new ResponseEntity<>("School with id " + id + " deleted.", HttpStatus.OK);
	}

	//hmm....muito a checkar aqui, nomeadamente os parametros....talvex um form aqui em vez de school.
	@Transactional
	public ResponseEntity<?> updateSchool(School school) {		
		return new ResponseEntity<>(schoolDao.update(school), HttpStatus.OK);
	}

	public ResponseEntity<?> addClass(Class schoolClass) {	
		return new ResponseEntity<>(classDao.add(schoolClass), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAllClasses() {

		List<Class> schoolClasses = classDao.getAll();

		if (schoolClasses.isEmpty())
			throw new ClassNotFoundException("Classes not found.");

		return new ResponseEntity<>(schoolClasses, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getClassById(HttpServletRequest request, int id) {

		Class schoolClass = classDao.getById(id);

		if (schoolClass == null)
			throw new ClassNotFoundException("Classe with id " + id + " not found.");

		return new ResponseEntity<>(schoolClass, HttpStatus.OK);
	}

	//	@Transactional
	//	public ResponseEntity<?> getClassByEmail(String email) {
	//		
	//		Class schoolClass = classDao.getByEmail(email);
	//		
	//		if (schoolClass == null)
	//			throw new ClassNotFoundException("Class with email " + email + " not found.");
	//		
	//		return new ResponseEntity<>(schoolClass, HttpStatus.OK);
	//	}

	@Transactional
	public ResponseEntity<?> deleteClass(int id) {

		classDao.delete(id);

		return new ResponseEntity<>("Class with id " + id + " deleted.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> updateClass(Class schoolClass) {
		return new ResponseEntity<>(classDao.update(schoolClass), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getClassesByTeacherId(HttpServletRequest request) {

		JwtUserPermissions userPermissions = retrievePermissions(request);

		List<Class> schoolClasses = classDao.getClassesByTeacherId(userPermissions.getId());

		if (schoolClasses.isEmpty())
			throw new ClassNotFoundException("Classes not found.");

		return new ResponseEntity<>(schoolClasses, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> hireTeacher(HttpServletRequest request, int teacherId) {

		JwtUserPermissions userPermissions = retrievePermissions(request);

		Teacher teacher = teacherDao.getById(teacherId);

		School school = schoolDao.getById(userPermissions.getSchoolId());

		teacher.setSchool(school);

		teacherDao.update(teacher);

		return new ResponseEntity<>("Teacher '" + teacher.getName() + "' is now teaching in '" + school.getName() + "'.", HttpStatus.OK);
	}

	public ResponseEntity<?> getClassesBySchool(int id, HttpServletRequest request) {

		List<Class> schoolClasses = classDao.getClassesBySchoolId(id);

		if (schoolClasses.isEmpty())
			throw new ClassNotFoundException("Classes not found at school with id " + id);

		return new ResponseEntity<>(schoolClasses, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getLocalAdmins() {

		List<Admin> localAdmins = adminDao.getLocalAdmins();

		if (localAdmins.isEmpty())
			throw new AdminNotFoundException("No Local Admins were found.");

		return new ResponseEntity<>(localAdmins, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> addLocalAdmin(HttpServletRequest request, RegistrationLocalAdminForm registrationLocalAdminForm) {

		School school = schoolDao.getById(registrationLocalAdminForm.getSchoolId());

		Admin admin = new Admin();

		admin.setName(registrationLocalAdminForm.getName());
		admin.setAddress(registrationLocalAdminForm.getAddress());
		admin.setPhonenumber(registrationLocalAdminForm.getPhonenumber());
		admin.setSchool(school);
		admin.setEmail(registrationLocalAdminForm.getEmail());
		admin.setPassword(registrationLocalAdminForm.getPassword());
		admin.setRole("ROLE_LOCAL_ADMIN");//MAIS TARDE PODE SER QUE MUDE ISTO.....PARA PUDER DAR ADD A GENERALS

		adminDao.add(admin);

		return new ResponseEntity<>("Local admin ´" + admin.getName() + "' added.", HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<?> removeLocalAdmin(HttpServletRequest request, int id) {

		adminDao.delete(id);

		return new ResponseEntity<>("Local admin with id " + id + " was fired!", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getLocalAdminById(HttpServletRequest request, int id) {
		return new ResponseEntity<>(adminDao.getById(id), HttpStatus.OK);
	}
	
	private JwtUserPermissions retrievePermissions(HttpServletRequest request) {

		for(Cookie cookie : request.getCookies()) {

			if(cookie.getName().equals("jwtToken"))
				return jwtHandler.getUserPermissions(cookie.getValue());
		}

		return null;
	}

}




















