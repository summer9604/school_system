package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.ricardo.school_system.entities.Subject;
import org.springframework.stereotype.Repository;

@Repository
public class SubjectDao extends GenericDao<Subject> {

	@Override
	@Transactional
	public Subject add(Subject subject) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.save(subject);
		
		return subject;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Subject> getAll() {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from subject";
		
		return session.createSQLQuery(query).addEntity(Subject.class).getResultList();
	}

	@Override
	@Transactional
	public Subject getById(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.get(Subject.class, id);
	}

	@Override
	@Transactional
	public Subject getByEmail(String email) {
		return null;
	}

	@Override
	@Transactional
	public List<Subject> getByName(String name) {
		return null;
	}
	
	@Transactional
	public Subject getSubjectByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
				
		String query = "Select * from subject where name = '" + name + "'";
		
		return (Subject) session.createSQLQuery(query).addEntity(Subject.class).getSingleResult();
	}

	@Override
	@Transactional
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		Subject subject = session.get(Subject.class, id);
		
		session.delete(subject);
	}

	@Override
	@Transactional
	public Subject update(Subject subject) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(subject);
		
		return subject;
	}

}
