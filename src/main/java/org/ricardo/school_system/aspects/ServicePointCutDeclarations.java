package org.ricardo.school_system.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class ServicePointCutDeclarations {

	@Pointcut("execution(* org.ricardo.school_system.services.StudentService.add(..))")
	public void checkAddStudentPermissions() {}
	
	@Pointcut("execution(* org.ricardo.school_system.services.StudentService.getById(*, *))")
	public void checkGetStudentByIdPermissions() {}
	
	@Pointcut("execution(* org.ricardo.school_system.services.SchoolService.hireTeacher(..))")
	public void checkHireTeacher() {}

	@Pointcut("execution(* org.ricardo.school_system.services.SchoolService.getClassById(..))")
	public void checkGetClassByIdPermissions() {}

	@Pointcut("execution(* org.ricardo.school_system.services.SchoolService.addLocalAdmin(..))")
	public void checkAddLocalAdminPermissions() {}

	@Pointcut("execution(* org.ricardo.school_system.services.StudentService.expelStudent(..))")
	public void checkExpelStudentPermissions() {}
	
	@Pointcut("execution(* org.ricardo.school_system.services.TeacherService.getByIdForAdmin(..))")
	public void checkGetTeacherByIdForAdmin() {}
		
	@Pointcut("execution(* org.ricardo.school_system.services.SchoolService.removeLocalAdmin(..))")
	public void checkRemoveLocalAdminPermissions() {}

	@Pointcut("execution( * org.ricardo.school_system.services.TeacherService.placeIntoClasses(..))")
	public void checkTeachersPlacementIntoClassesPermissions() {}

	@Pointcut("execution(* org.ricardo.school_system.services.StudentService.giveGradeToStudent(..))")
	public void checkGiveGradeToStudentPermissions() {}
}
