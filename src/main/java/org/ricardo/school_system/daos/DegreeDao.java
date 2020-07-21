package org.ricardo.school_system.daos;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.entities.Degree;
import org.springframework.stereotype.Repository;

@Repository
public class DegreeDao extends GenericDao<Degree> {

	@Override
	public Degree add(Degree degree) {
		
		Session session = sessionFactory.getCurrentSession();

		session.save(degree);
		
		return degree;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Degree> getAll() {
		
		Session session = sessionFactory.getCurrentSession();

		Query<Degree> query = session.createQuery("from Degree");
		
		return (List<Degree>) query.getResultList();
	}

	@Override
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
	
	@SuppressWarnings("unchecked")
	public Degree getDegreeByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Degree> query = session.createQuery("from Degree where name=:name");
		
		query.setParameter("name", name);
				
		return (Degree) query.uniqueResult();
	}

	@Override
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		Degree degree = session.get(Degree.class, id);
		
		session.delete(degree);
	}

	@Override
	public Degree update(Degree degree) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(degree);

		return degree;
	}

	@SuppressWarnings("unchecked")
	public List<Degree> getDegreesBySchoolId(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from degree where idDegree in(\r\n" + 
				"Select degree_id from school_degree where school_id = " + id + ");";

		return session.createSQLQuery(query).addEntity(Degree.class).getResultList();
	}
	
}















