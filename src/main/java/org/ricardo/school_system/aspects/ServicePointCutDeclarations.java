package org.ricardo.school_system.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class ServicePointCutDeclarations {

	@Pointcut("execution(* org.ricardo.school_system.services.SchoolService.hireTeacher(..))")
	public void checkHireTeacher() {}
	
	@Pointcut("execution(* org.ricardo.school_system.services.SchoolService.getClassById(..))")
	public void checkGetClassByIdPermissions() {}
	
	@Pointcut("execution(* org.ricardo.school_system.services.TeacherService.getByIdForAdmin(..))")
	public void checkGetTeacherByIdForAdmin() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.SchoolController.getTeacher*(..))")
	public void getTeachersEndPoint() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.SchoolController.getTeacherById(..))")
	public void getTeacherByIdEndPoint() {}
	
}
