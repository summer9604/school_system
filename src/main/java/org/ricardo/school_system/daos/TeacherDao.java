package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.springframework.stereotype.Repository;

@Repository
public class TeacherDao extends GenericDao<Teacher> {

	@Override
	@Transactional
	public Teacher add(Teacher teacher) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.save(teacher);
		
		return teacher;
	}
	
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Teacher> getAll() {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from teacher";
		
		return session.createSQLQuery(query).addEntity(Teacher.class).getResultList();
	}

	@Transactional
	public Subject getSubject(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Teacher teacher = session.get(Teacher.class, id);
		
		return teacher.getSubject();
	}
	
	@Override
	@Transactional
	public Teacher getById(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.get(Teacher.class, id);
	}

	@Override
	@Transactional
	public Teacher getByEmail(String email) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from teacher where email = '" + email + "'";
		
		return (Teacher) session.createSQLQuery(query).addEntity(Teacher.class).getSingleResult();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Teacher> getByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from teacher where name = '" + name + "'";
		
		return (List<Teacher>) session.createSQLQuery(query).addEntity(Teacher.class).getResultList();
	}

	@Override
	@Transactional
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		Teacher teacher = session.get(Teacher.class, id);
		
		session.delete(teacher);
	}

	@Override
	@Transactional
	public Teacher update(Teacher teacher) {
		
		Session session = sessionFactory.getCurrentSession();
		
	    session.saveOrUpdate(teacher);
	    
	    return teacher;
	}

}
