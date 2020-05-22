package org.ricardo.school_system.controllers;

import java.util.List;
import org.ricardo.school_system.assemblers.DegreeSubjectBundle;
import org.ricardo.school_system.assemblers.SchoolInfo;
import org.ricardo.school_system.assemblers.TeacherInfo;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Degree;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.services.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@Autowired
	private DegreeService degreeService;
	
	@Autowired
	private TeacherDao teacherDao;
	
	@Autowired
	private SubjectDao subjectDao;
	
	@Autowired
	private SchoolDao schoolDao;
	
	@GetMapping("/")
	public List<Teacher> getTeachers() {
		return teacherDao.getAll();
	}
	
	@GetMapping("/subjects/{id}")
	public Subject getSubject(@PathVariable("id") int id){
		return teacherDao.getSubject(id);
	}
	
	@PostMapping("/teachers")
	public Teacher addTeacher(@RequestBody TeacherInfo teacherInfo) {
		
		Subject subject = subjectDao.getById(teacherInfo.getSubjectId());
		
		Teacher teacher = new Teacher(teacherInfo.getName(), teacherInfo.getAddress(), 
						              teacherInfo.getPhonenumber(), teacherInfo.getEmail(), subject);
		
		return teacherDao.add(teacher);
	}
	
	@PostMapping("/degrees")
	public Degree addDegree(@RequestBody DegreeSubjectBundle degreeSubjectBundle) {
		
		return degreeService.addDegreeWithSubjects(degreeSubjectBundle);
	}
	
	@PostMapping("/teachers/{id}")
	public School addSchoolToTeacher(@PathVariable("id") int id, @RequestBody SchoolInfo schoolInfo) {
		
		Teacher teacher = teacherDao.getById(id);
		
		School school = schoolDao.getSchoolByName(schoolInfo.getName());
		
		teacher.setSchool(school);
		
		teacherDao.update(teacher);
		
		return school;
	}
	
}





