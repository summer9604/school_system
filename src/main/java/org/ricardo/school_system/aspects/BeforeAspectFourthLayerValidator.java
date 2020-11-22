package org.ricardo.school_system.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.dto.DataAndPermissions;
import org.ricardo.school_system.dto.JwtUserPermissions;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.TeacherNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(4)
public class BeforeAspectFourthLayerValidator extends GenericAspect {

	@Autowired
	private SchoolDao schoolDao;

	@Autowired
	private TeacherDao teacherDao;

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetTeacherByIdForAdmin()")
	public void validateAdminTokensForGetTeacherById(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);

		JwtUserPermissions userPermissions = (JwtUserPermissions) dataAndPermissions.getUserPermissions();
		
		int teacherId = (int) dataAndPermissions.getData();

		Teacher teacher = teacherDao.getById(teacherId);

		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with id " + teacherId + " not found.");

		School schoolTeacher = schoolDao.getSchoolByTeacherId(teacher.getId());

		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") && userPermissions.getSchoolId() != schoolTeacher.getId())
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");		
	}

	@SuppressWarnings("rawtypes")
	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkHireTeacher()")
	public void checkIfTeacherIsEmployedAlready(JoinPoint joinPoint) {

		DataAndPermissions dataAndPermissions = getDataAndPermissions(joinPoint, 1);
		
		int teacherId = (int) dataAndPermissions.getData();
		
		Teacher teacher = teacherDao.getById(teacherId); 

		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with id " + teacherId + " not found.");

		School schoolTeacher = schoolDao.getSchoolByTeacherId(teacher.getId());

		if (schoolTeacher.getId() > 0) 
			throw new OperationNotAuthorizedException("Teacher is currently employed.");
	}
}


















