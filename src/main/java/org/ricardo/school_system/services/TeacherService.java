package org.ricardo.school_system.services;

import java.util.List;
import javax.transaction.Transactional;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

	@Autowired
	private TeacherDao teacherDao;
	
	public Teacher add(Teacher teacher) {		
		return teacherDao.add(teacher);
	}

	@Transactional
	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	@Transactional
	public Teacher getById(int id) {
		return teacherDao.getById(id);
	}

	@Transactional
	public Teacher getByEmail(String email) {
		return teacherDao.getByEmail(email);
	}

	@Transactional
	public List<Teacher> getByName(String name) {
		return teacherDao.getByName(name);
	}

	@Transactional
	public void delete(int id) {
		teacherDao.delete(id);
	}

	@Transactional
	public Teacher update(Teacher teacher) {
		return teacherDao.update(teacher);
	}
}
