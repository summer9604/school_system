package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.ricardo.school_system.entities.Class;

@Repository
public class ClassDao extends GenericDao<Class> {

	@Override
	@Transactional
	public Class add(Class schoolClass) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(schoolClass);
		
		return schoolClass;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Class> getAll() {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from class";

		return session.createSQLQuery(query).addEntity(Class.class).getResultList(); 
	}

	@Override
	@Transactional
	public Class getById(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		return session.get(Class.class, id);
	}

	@Override
	@Transactional
	public Class getByEmail(String email) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from class where email = '" + email + "'";
		
		return (Class) session.createSQLQuery(query).addEntity(Class.class).getSingleResult();
	}

	@Override
	@Transactional
	public List<Class> getByName(String name) {
		return null;
	}

	@Override
	@Transactional
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		Class schoolClass = session.get(Class.class, id);
		
		session.delete(schoolClass);
	}

	@Override
	@Transactional
	public Class update(Class schoolClass) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(schoolClass);
		
		return schoolClass;
	}

}
