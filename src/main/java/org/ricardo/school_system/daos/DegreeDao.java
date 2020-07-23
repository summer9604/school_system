package org.ricardo.school_system.daos;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.entities.Degree;
import org.springframework.stereotype.Repository;

@Repository
public class DegreeDao extends GenericDao<Degree> {
	
	@SuppressWarnings("unchecked")
	public Degree getDegreeByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Degree> query = session.createQuery("from Degree where name=:name");
		
		query.setParameter("name", name);
				
		return (Degree) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Degree> getDegreesBySchoolId(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from degree where idDegree in(\r\n" + 
				"Select degree_id from school_degree where school_id = " + id + ");";

		return session.createSQLQuery(query).addEntity(Degree.class).getResultList();
	}
	
}















