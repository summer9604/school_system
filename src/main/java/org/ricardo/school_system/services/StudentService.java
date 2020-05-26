package org.ricardo.school_system.services;

import java.util.List;
import javax.transaction.Transactional;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

	@Autowired
	private StudentDao studentDao;
	
	public Student add(Student student) {		
		return studentDao.add(student);
	}

	@Transactional
	public List<Student> getAll() {
		return studentDao.getAll();
	}

	@Transactional
	public Student getById(int id) {
		return studentDao.getById(id);
	}

	@Transactional
	public Student getByEmail(String email) {
		return studentDao.getByEmail(email);
	}

	@Transactional
	public List<Student> getByName(String name) {
		return studentDao.getByName(name);
	}

	@Transactional
	public void delete(int id) {
		studentDao.delete(id);
	}

	@Transactional
	public Student update(Student student) {
		return studentDao.update(student);
	}
}
