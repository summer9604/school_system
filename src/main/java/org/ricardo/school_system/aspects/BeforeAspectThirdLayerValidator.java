package org.ricardo.school_system.aspects;

import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.AdminDao;
import org.ricardo.school_system.daos.ClassDao;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.dto.DataAndPermissions;
import org.ricardo.school_system.dto.RegistrationLocalAdminForm;
import org.ricardo.school_system.dto.RegistrationStudentForm;
import org.ricardo.school_system.dto.StudentGradeForm;
import org.ricardo.school_system.dto.TeacherClassForm;
import org.ricardo.school_system.entities.Admin;
import org.ricardo.school_system.entities.Class;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Student;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.AdminNotFoundException;
import org.ricardo.school_system.exceptions.ClassNotFoundException;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.SchoolNotFoundException;
import org.ricardo.school_system.exceptions.StudentNotFoundException;
import org.ricardo.school_system.exceptions.TeacherNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(3)
public class BeforeAspectThirdLayerValidator extends GenericAspect { 

	@Autowired
	private ClassDao classDao;

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private SchoolDao schoolDao;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private SubjectDao subjectDao;

	@Autowired
	private TeacherDao teacherDao;

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getGeneralAdminEndPoints()")
	public void validateGeneralAdminTokens(JoinPoint joinPoint) {

		JwtUserPermissions userPermissions = getUserPermissions(joinPoint);

		if (!userPermissions.getPermissions().equals("ROLE_GENERAL_ADMIN"))
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");
	}

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getLocalAdminEndPoints()")
	public void validateLocalAdminTokens(JoinPoint joinPoint) {

		JwtUserPermissions userPermissions = getUserPermissions(joinPoint);

		if (!userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN"))
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetClassByIdPermissions()")
	public void checkGetClassByIdPermissions(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		Integer classId = (Integer) dataAndPermissions.getData();

		if (classId == null) 
			throw new ClassNotFoundException("Class not found.");

		Class schoolClass = classDao.getById(classId);

		if (schoolClass == null) 
			throw new ClassNotFoundException("Class not found.");

		if (userPermissions.getPermissions().equals("ROLE_STUDENT"))
			throw new OperationNotAuthorizedException("You dont' have enough permissions");	

		if (userPermissions.getPermissions().equals("ROLE_TEACHER") &&
				classDao.getClassTeacherRelation(userPermissions.getId(), schoolClass.getId()) == null)
			throw new OperationNotAuthorizedException("You dont' have enough permissions");	

		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") && 
				schoolDao.getSchoolByLocalAdminIdAndClassId(userPermissions.getId(), schoolClass.getId()) == null) 
			throw new OperationNotAuthorizedException("You dont' have enough permissions");			
	}


	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetStudentByIdPermissions()")
	public void checkGetStudentByIdPermissions(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		int studentId = (int) dataAndPermissions.getData();

		if (userPermissions.getPermissions().equals("ROLE_STUDENT") && userPermissions.getId() != studentId)
			throw new OperationNotAuthorizedException("You don´t have enough permissions");

		Student student = studentDao.getById(studentId);

		if (student == null)
			throw new StudentNotFoundException("Student with id " + studentId + " not found.");

		Class studentClass = classDao.getClassByStudentId(studentId);

		if (studentClass == null && !userPermissions.getPermissions().equals("ROLE_GENERAL_ADMIN"))
			throw new OperationNotAuthorizedException("Access denied. This student is under General admin supervision.");

		if (studentClass != null) {

			if (userPermissions.getPermissions().equals("ROLE_TEACHER")) {

				Class teacherClass = classDao.getClassTeacherRelation(userPermissions.getId(), studentClass.getId());

				if (teacherClass == null)
					throw new OperationNotAuthorizedException("You don't have enough permissions");
			}

			if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN")) {

				School schoolClass = schoolDao.getSchoolByClassId(studentClass.getId());

				if (schoolClass.getId() != userPermissions.getSchoolId())
					throw new OperationNotAuthorizedException("Student with id " + studentId + " belongs to other school.");
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkTeachersPlacementIntoClassesPermissions()")
	public void checkTeachersPlacementIntoClassesPermissions(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, new TeacherClassForm());

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		TeacherClassForm teacherClassForm = (TeacherClassForm) dataAndPermissions.getData();

		int teacherId = teacherClassForm.getTeachedId();

		School schoolTeacher = schoolDao.getSchoolByTeacherId(teacherId);

		if (!userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") || userPermissions.getSchoolId() != schoolTeacher.getId())
			throw new OperationNotAuthorizedException("You don´t have enough permissions");

		Teacher teacher = teacherDao.getById(teacherId);

		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with id " + teacherId + " not found.");

		Subject teacherSubject = subjectDao.getTeacherSubject(teacher.getId());

		List<Integer> classesId = teacherClassForm.getClassesId();

		for(int classId : classesId) {

			School schoolClass = schoolDao.getSchoolByClassId(classId);

			if (schoolClass.getId() != schoolTeacher.getId())
				throw new OperationNotAuthorizedException("Teacher with id " + teacherId + " and class with id " + classId + " are not placed in the same school");

			if (classDao.getClassTeacherRelation(teacherId, classId) != null)
				throw new OperationNotAuthorizedException("Teacher with id " + teacherId + " already have a class with id " + classId);

			if (subjectDao.getSubjectFromClass(teacherSubject, classId) == null)
				throw new OperationNotAuthorizedException("Class with id " + classId  + " doesn't have '" + teacherSubject.getName() + "' on its schedule.");

			if (teacherDao.getTeacherFromClassBySubjectIdAndClassId(teacherSubject, classId) != null)
				throw new OperationNotAuthorizedException("Class with id " + classId  + " already has a teacher that teaches '" + teacherSubject.getName() + "'");
		}		
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGiveGradeToStudentPermissions()")
	public void checkGiveGradeToStudentPermissions(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, new StudentGradeForm());

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		StudentGradeForm studentGradeForm = (StudentGradeForm) dataAndPermissions.getData();

		if (!userPermissions.getPermissions().equals("ROLE_TEACHER"))
			throw new OperationNotAuthorizedException("You don´t have enough permissions");

		if (studentDao.getById(studentGradeForm.getStudentId()) == null)
			throw new StudentNotFoundException("Student with id " + studentGradeForm.getStudentId() + " not found");

		if (userPermissions.getPermissions().equals("ROLE_TEACHER")) {

			Subject teacherSubject = subjectDao.getTeacherSubject(userPermissions.getId());

			Teacher teacher = teacherDao.getStudentTeacherByStudentIdAndSubjectId(studentGradeForm.getStudentId(), teacherSubject.getId());

			if (teacher == null) 
				throw new OperationNotAuthorizedException("You don´t have enough permissions (NOT YOUR STUDENT)");
		}
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkAddLocalAdminPermissions()")
	public void checkAddLocalAdminPermissions(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, new RegistrationLocalAdminForm());

		RegistrationLocalAdminForm registrationLocalAdminForm = (RegistrationLocalAdminForm) dataAndPermissions.getData();
		
		int schoolId = registrationLocalAdminForm.getSchoolId();

		School school = schoolDao.getById(schoolId);

		if (school == null)
			throw new SchoolNotFoundException("School with id " + schoolId + " not found");
	
		checkPhoneNumberAndEmailAvailability(registrationLocalAdminForm.getEmail(), registrationLocalAdminForm.getPhonenumber());
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkAddStudentPermissions()")
	public void checkAddStudentPermissions(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, new RegistrationStudentForm());

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		RegistrationStudentForm registrationStudentForm = (RegistrationStudentForm) dataAndPermissions.getData();

		checkPhoneNumberAndEmailAvailability(registrationStudentForm.getEmail(), registrationStudentForm.getPhonenumber());

		int classId = registrationStudentForm.getClassId();
		int schoolId = userPermissions.getSchoolId();

		Class schoolClass = classDao.getClassByClassAndSchoolRelation(classId, schoolId);

		if (schoolClass == null)
			throw new OperationNotAuthorizedException("Class with id " +  classId + " does not belong to your school - (School " + schoolId + ").");
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkRemoveLocalAdminPermissions()")
	public void checkRemoveLocalAdminPermissions(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		int adminId = (int) dataAndPermissions.getData();

		Admin admin = adminDao.getById(adminId);

		if (admin == null)
			throw new AdminNotFoundException("Admin with id " + adminId + " not found.");

		if (admin.getRole().equals("ROLE_GENERAL_ADMIN"))
			throw new OperationNotAuthorizedException("You can't remove a General Admin.");
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkExpelStudentPermissions()")
	public void checkExpelStudentPermissions(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		int studentId = (int) dataAndPermissions.getData();

		Student student = studentDao.getById(studentId);

		if (student == null)
			throw new StudentNotFoundException("Student with id " + studentId + " not found.");

		School studentSchool = schoolDao.getSchoolByStudentId(studentId);

		int adminSchoolId = userPermissions.getSchoolId();

		if (studentSchool.getId() != adminSchoolId)
			throw new OperationNotAuthorizedException("Access denied. This student doesn't belong to your school - (School " + adminSchoolId + ").");
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetGradesByStudentId()")
	public void checkGetGradesByStudentId(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		int studentId = (int) dataAndPermissions.getData();

		Student student = studentDao.getById(studentId);

		if (student == null)
			throw new StudentNotFoundException("Student with id " + studentId + " not found.");

		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") && 
				schoolDao.getSchoolByStudentId(studentId).getId() != userPermissions.getSchoolId())
			throw new OperationNotAuthorizedException("Access denied. This student doesn't belong to your school - (School " + userPermissions.getSchoolId() + ").");

		if (userPermissions.getPermissions().equals("ROLE_TEACHER")) {

			Subject teacherSubject = subjectDao.getTeacherSubject(userPermissions.getId());

			if (teacherDao.getStudentTeacherByStudentIdAndSubjectId(studentId, teacherSubject.getId()) == null)
				throw new OperationNotAuthorizedException("Access denied. This is not a student from one of your classes.");
		}
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetLocalAdminById()")
	public void checkGetLocalAdminById(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		int adminId = (int) dataAndPermissions.getData();

		if (adminId == userPermissions.getId())
			throw new OperationNotAuthorizedException("WHY ARE YOU LOOKING FOR YOURSELF?!!!!");

		Admin admin = adminDao.getById(adminId);

		if (admin == null)
			throw new AdminNotFoundException("Admin with id " + adminId + " not found.");

		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") && admin.getRole().equals("ROLE_GENERAL_ADMIN"))
			throw new OperationNotAuthorizedException("Access denied.");
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkTeacherCredentialsToGetStudentsByClassId()")
	public void checkTeacherCredentialsToGetStudentsByClassId(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		int classId = (int) dataAndPermissions.getData();

		Class schoolClass = classDao.getClassTeacherRelation(userPermissions.getId(), classId);

		if (schoolClass == null)
			throw new OperationNotAuthorizedException("Class with id " + classId + " doesn't belong to teacher with id " + userPermissions.getId());
	}
	
	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkUpdateTeacherLocalAdminPermissions()")
	public void checkUpdateTeacherLocalAdminPermissions(JoinPoint joinPoint) {
		
		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		int teacherId = (int) dataAndPermissions.getData();
		
		int localAdminSchoolId = userPermissions.getSchoolId();
		
		Teacher teacher = teacherDao.getById(teacherId);
		
		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with id" + teacherId + " not found.");
		
		School school = schoolDao.getSchoolByTeacherId(teacherId);
		
		if (school == null)
			throw new OperationNotAuthorizedException("This teacher does not belong to your school. Actual status: Unemployed.");
		
		if (school.getId() != localAdminSchoolId)
			throw new OperationNotAuthorizedException("Access denied. This is not a teacher of your school.");
	}
	
	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkUpdateStudentLocalAdminPermissions()")
	public void checkUpdateStudentLocalAdminPermissions(JoinPoint joinPoint) {
		
		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();

		int studentId = (int) dataAndPermissions.getData();
		
		int localAdminSchoolId = userPermissions.getSchoolId();
		
		Student student = studentDao.getById(studentId);
		
		if (student == null)
			throw new TeacherNotFoundException("Student with id" + studentId + " not found.");
		
		School school = schoolDao.getSchoolByStudentId(studentId);
		
		if (school == null)
			throw new OperationNotAuthorizedException("This student does not belong to your school. Actual status: Under General Admin supervision.");
		
		if (school.getId() != localAdminSchoolId)
			throw new OperationNotAuthorizedException("Access denied. This is not a student of your school.");
	}

	private void checkPhoneNumberAndEmailAvailability(String email, int phonenumber) {
		
		String emailAlreadyExists = schoolDao.checkEmailAvailability(email);		

		if (emailAlreadyExists != null)
			throw new OperationNotAuthorizedException("Email '" + email + "' alreadys exists");

		Integer phoneNumberAlreadyExists = schoolDao.checkPhoneNumberAvailability(phonenumber);

		if (phoneNumberAlreadyExists != null)
			throw new OperationNotAuthorizedException("Phone Number '" + phonenumber + "' alreadys exists");
	}
	
}




















