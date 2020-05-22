package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.ricardo.school_system.entities.School;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolDao extends GenericDao<School> {

	@Override
	@Transactional
	public School add(School school) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(school);

		return school;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<School> getAll() {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from school";

		return session.createSQLQuery(query).addEntity(School.class).getResultList(); 
	}

	@Override
	@Transactional
	public School getById(int id) {

		Session session = sessionFactory.getCurrentSession();

		return session.get(School.class, id);
	}

	@Override
	public School getByEmail(String email) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from school where email = '" + email + "'";
		
		return (School) session.createSQLQuery(query).addEntity(School.class).getSingleResult();
	}

	@Override
	@Transactional
	public List<School> getByName(String name) {
		return null;
	}
	
	@Transactional
	public School getSchoolByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from school where name = '" + name + "'";
		
		return (School) session.createSQLQuery(query).addEntity(School.class).getSingleResult();
	}

	@Override
	@Transactional
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		School school = session.get(School.class, id);
		
		session.delete(school);
	}

	@Override
	@Transactional
	public School update(School school) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(school);
		
		return school;
	}

}
