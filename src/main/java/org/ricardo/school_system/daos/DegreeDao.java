package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.ricardo.school_system.entities.Degree;
import org.springframework.stereotype.Repository;

@Repository
public class DegreeDao extends GenericDao<Degree> {

	@Override
	@Transactional
	public Degree add(Degree degree) {
		
		Session session = sessionFactory.getCurrentSession();

		session.save(degree);
		
		return degree;
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Degree> getAll() {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from degree";
		
		return session.createSQLQuery(query).addEntity(Degree.class).getResultList();
	}

	@Override
	@Transactional
	public Degree getById(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		return session.get(Degree.class, id);
	}

	@Override
	public Degree getByEmail(String email) {
		return null;
	}

	@Override
	public List<Degree> getByName(String name) {
		return null;
	}
	
	@Transactional
	public Degree getDegreeByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from degree where name = '" + name + "'";
		
		return (Degree) session.createSQLQuery(query).addEntity(Degree.class).getSingleResult();
	}

	@Override
	@Transactional
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		Degree degree = session.get(Degree.class, id);
		
		session.delete(degree);
		
	}

	@Override
	@Transactional
	public Degree update(Degree degree) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(degree);

		return degree;
	}

}
