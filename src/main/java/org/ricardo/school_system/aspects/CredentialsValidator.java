package org.ricardo.school_system.aspects;

import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.assemblers.TeacherClassForm;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.AdminDao;
import org.ricardo.school_system.daos.ClassDao;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Class;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Student;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.ClassNotFoundException;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.StudentNotFoundException;
import org.ricardo.school_system.exceptions.TeacherNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
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
	private JwtHandler jwtHandler;

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

		Class studentClass = classDao.getClassByStudentId(studentId);//ALUNO TEM QUE TER SEMPRE UMA TURMA...

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

		School schoolTeacher = schoolDao.getSchoolByTeacherId(teacherId);//nullpoint porque?????

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

}













