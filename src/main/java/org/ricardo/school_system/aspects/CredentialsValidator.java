package org.ricardo.school_system.aspects;

import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.assemblers.RegistrationLocalAdminForm;
import org.ricardo.school_system.assemblers.RegistrationStudentForm;
import org.ricardo.school_system.assemblers.StudentGradeForm;
import org.ricardo.school_system.assemblers.TeacherClassForm;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.AdminDao;
import org.ricardo.school_system.daos.ClassDao;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.daos.StudentSubjectDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Admin;
import org.ricardo.school_system.entities.Class;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Student;
import org.ricardo.school_system.entities.StudentSubject;
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
public class CredentialsValidator extends GenericAspect {

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

	@Autowired
	private StudentSubjectDao studentSubjectDao;

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getGeneralAdminEndPoints()")
	public void validateGeneralAdminTokens(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);

		if (!permissions.getPermissions().equals("ROLE_GENERAL_ADMIN"))
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");
	}

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getTeachersEndPoint()")
	public void validateAdminTokensForGetTeachers(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);

		if (!permissions.getPermissions().equals("ROLE_GENERAL_ADMIN") && !permissions.getPermissions().equals("ROLE_LOCAL_ADMIN"))
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");		
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetClassByIdPermissions()")
	public void checkGetClassByIdPermissions(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions userPermissions = jwtHandler.getUserPermissions(token);

		int classId = 0;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof Integer) classId = (int) arg;
		}

		if (classId <= 0) 
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
				adminDao.getSchoolIdByLocalAdminId(userPermissions.getId()) != classDao.getSchoolIdByClassId(schoolClass.getId())) 
			throw new OperationNotAuthorizedException("You dont' have enough permissions");			
	}


	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetStudentByIdPermissions()")
	public void checkGetStudentByIdPermissions(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions userPermissions = jwtHandler.getUserPermissions(token);

		int studentId = 0;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof Integer) studentId = (int) arg;
		}

		if (userPermissions.getPermissions().equals("ROLE_STUDENT") && userPermissions.getId() != studentId)
			throw new OperationNotAuthorizedException("You don´t have enough permissions");

		Student student = studentDao.getById(studentId);

		if (student == null)
			throw new StudentNotFoundException("Student with id " + studentId + " not found.");

		Class studentClass = classDao.getClassByStudentId(studentId);
		
		if (studentClass == null && !userPermissions.getPermissions().equals("ROLE_GENERAL_ADMIN"))
			throw new OperationNotAuthorizedException("Access denied. This student is under General admin supervision.");

		if (userPermissions.getPermissions().equals("ROLE_TEACHER")) {

			Class teacherClass = classDao.getClassTeacherRelation(userPermissions.getId(), studentClass.getId());

			if (teacherClass == null)
				throw new OperationNotAuthorizedException("You don't have enough permissions");

			if (studentClass.getId() != teacherClass.getId())
				throw new OperationNotAuthorizedException("You don´t have enough permissions");
		}

		School schoolClass = schoolDao.getSchoolByClassId(studentClass.getId());

		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") && schoolClass.getId() != userPermissions.getSchoolId())
			throw new OperationNotAuthorizedException("You don´t have enough permissions");
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkTeachersPlacementIntoClassesPermissions()")
	public void checkTeachersPlacementIntoClassesPermissions(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions userPermissions = jwtHandler.getUserPermissions(token);

		TeacherClassForm teacherClassForm = null;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof TeacherClassForm) 	
				teacherClassForm = (TeacherClassForm) arg;
		}

		int teacherId = teacherClassForm.getTeachedId();

		School schoolTeacher = schoolDao.getSchoolByTeacherId(teacherId);

		if (!userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") ||
				(userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") &&
						userPermissions.getSchoolId() != schoolTeacher.getId()))
			throw new OperationNotAuthorizedException("You don´t have enough permissions");

		Teacher teacher = teacherDao.getById(teacherId);

		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with id " + teacherId + " not found.");

		Subject teacherSubject = subjectDao.getTeacherSubject(teacher.getId());

		List<Integer> classesId = teacherClassForm.getClassesId();

		for(int classId : classesId) {

			int classSchoolId = classDao.getSchoolIdByClassId(classId);

			if (classSchoolId != schoolTeacher.getId())
				throw new OperationNotAuthorizedException("Teacher with id " + teacherId + " and class with id " + classId + " are not placed in the same school");

			if (classDao.getClassTeacherRelation(teacherId, classId) != null)
				throw new OperationNotAuthorizedException("Teacher with id " + teacherId + " already have class with id " + classId);

			if (subjectDao.getSubjectFromClass(teacherSubject, classId) == null)
				throw new OperationNotAuthorizedException("Class with id " + classId  + " doesn't have '" + teacherSubject.getName() + "' on its schedule.");

			if (teacherDao.getTeacherFromClass(teacherSubject, classId) != null)
				throw new OperationNotAuthorizedException("Class with id " + classId  + " already has a teacher that teaches '" + teacherSubject.getName() + "'");
		}		
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGiveGradeToStudentPermissions()")
	public void checkGiveGradeToStudentPermissions(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions userPermissions = jwtHandler.getUserPermissions(token);

		StudentGradeForm studentGradeForm = null;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof StudentGradeForm) 	
				studentGradeForm = (StudentGradeForm) arg;
		}

		if (!userPermissions.getPermissions().equals("ROLE_TEACHER"))
			throw new OperationNotAuthorizedException("You don´t have enough permissions");

		if (studentDao.getById(studentGradeForm.getStudentId()) == null)
			throw new StudentNotFoundException("Student with id " + studentGradeForm.getStudentId() + " not found");

		StudentSubject studentSubject = studentSubjectDao.getByStudentIdAndTeacherId(userPermissions.getId(), studentGradeForm.getStudentId());

		if (userPermissions.getPermissions().equals("ROLE_TEACHER") && studentSubject == null)
			throw new OperationNotAuthorizedException("You don´t have enough permissions (NOT YOUR STUDENT)");
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkAddLocalAdminPermissions()")
	public void checkAddLocalAdminPermissions(JoinPoint joinPoint) {

		RegistrationLocalAdminForm registrationLocalAdminForm = null;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof RegistrationLocalAdminForm) 	
				registrationLocalAdminForm = (RegistrationLocalAdminForm) arg;
		}

		int schoolId = registrationLocalAdminForm.getSchoolId();

		School school = schoolDao.getById(schoolId);

		if (school == null)
			throw new SchoolNotFoundException("School with id " + schoolId + " not found");
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkAddStudentPermissions()")
	public void checkAddStudentPermissions(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions userPermissions = jwtHandler.getUserPermissions(token);

		RegistrationStudentForm registrationStudentForm = null;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof RegistrationStudentForm) 	
				registrationStudentForm = (RegistrationStudentForm) arg;
		}

		String studentEmail = registrationStudentForm.getEmail();

		Student studentWithSameEmail = studentDao.getByEmail(studentEmail);

		if (studentWithSameEmail != null)
			throw new OperationNotAuthorizedException("Student with email '" + studentEmail + "' alreadys exists");

		int studentPhonenumber = registrationStudentForm.getPhonenumber();

		Student studentWithSamePhonenumber = studentDao.getByPhonenumber(studentPhonenumber);

		if (studentWithSamePhonenumber != null)
			throw new OperationNotAuthorizedException("Student with phonenumber '" + studentPhonenumber + "' alreadys exists");

		int classId = registrationStudentForm.getClassId();
		int schoolId = userPermissions.getSchoolId();

		Class schoolClass = classDao.getClassIdAndSchoolId(classId, schoolId);

		if (schoolClass == null)
			throw new OperationNotAuthorizedException("Class with id " +  classId + " does not belong to school with id" + schoolId);
	}
	
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkRemoveLocalAdminPermissions()")
	public void checkRemoveLocalAdminPermissions(JoinPoint joinPoint) {
		
		int adminId = 0;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof Integer) adminId = (int) arg;
		}

		Admin admin = adminDao.getById(adminId);
			
		if (admin == null)
			throw new AdminNotFoundException("Admin with id " + adminId + " not found.");
		
		if (admin.getRole().equals("ROLE_GENERAL_ADMIN"))
			throw new OperationNotAuthorizedException("You can't remove a General Admin.");
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkExpelStudentPermissions()")
	public void checkExpelStudentPermissions(JoinPoint joinPoint) {
		
		String token = getToken(joinPoint);

		JwtUserPermissions userPermissions = jwtHandler.getUserPermissions(token);
		
		int studentId = 0;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof Integer) studentId = (int) arg;
		}
		
		Student student = studentDao.getById(studentId);
		
		if (student == null)
			throw new StudentNotFoundException("Student with id " + studentId + " not found.");
		
		School studentSchool = schoolDao.getSchoolByStudentId(studentId);
		
		int adminSchoolId = userPermissions.getSchoolId();
		
		if (studentSchool.getId() != adminSchoolId)
			throw new OperationNotAuthorizedException("Access denied. This student doesn't belong to the school with id " + adminSchoolId + ".");
		
	}
	
}




















