package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.ricardo.school_system.entities.Student;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDao extends GenericDao<Student> {

	@Override
	@Transactional
	public Student add(Student student) {
		
		Session session = sessionFactory.getCurrentSession();

		session.save(student);
		
		return student;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Student> getAll() {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from student";
		
		return session.createSQLQuery(query).addEntity(Student.class).getResultList();
	}

	@Override
	@Transactional
	public Student getById(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		return session.get(Student.class, id);
	}

	@Override
	@Transactional
	public Student getByEmail(String email) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from student where email = '" + email + "'";
		
		return (Student) session.createSQLQuery(query).addEntity(Student.class).getSingleResult();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Student> getByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from student where name = '" + name + "'";
		
		return (List<Student>) session.createSQLQuery(query).addEntity(Student.class).getResultList();
	}

	@Override
	@Transactional
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		Student student = session.get(Student.class, id);
		
		session.delete(student);
	}

	@Override
	@Transactional
	public Student update(Student student) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(student);
		
		return student;
	}

}
