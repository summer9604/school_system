package org.ricardo.school_system.daos;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.entities.Student;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDao extends GenericDao<Student> {

	@Override
	public Student add(Student student) {
		
		Session session = sessionFactory.getCurrentSession();

		session.save(student);
		
		return student;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Student> getAll() {
		
		Session session = sessionFactory.getCurrentSession();

		Query<Student> query = session.createQuery("from Student");
		
		return (List<Student>) query.getResultList();
	}

	@Override
	public Student getById(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		return session.get(Student.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Student getByEmail(String email) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Student> query = session.createQuery("from Student where email=:email");
		
		query.setParameter("email", email);
				
		return (Student) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public Student getByEmailAndPassword(LoginForm loginInfo) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Student> query = session.createQuery("from Student where email=:email and password=:password");
		
		query.setParameter("email", loginInfo.getEmail());
		query.setParameter("password", loginInfo.getPassword());

		return (Student) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Student> getStudentsByTeacherId(int teacherId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from student where class_id in (\r\n" + 
				"Select class_id from teacher_class \r\n" + 
				"where teacher_id = " + teacherId + ");";

		return (List<Student>) session.createSQLQuery(query).addEntity(Student.class).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Student> getByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();

		Query<Student> query = session.createQuery("from Student where name=:name");

		query.setParameter("name", name);
		
		return (List<Student>) query.getResultList();
	}

	@Override
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		Student student = session.get(Student.class, id);
		
		session.delete(student);
	}

	@Override
	public Student update(Student student) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(student);
		
		return student;
	}

}
