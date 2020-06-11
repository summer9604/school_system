package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
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

		Query<School> query = session.createQuery("from School");

		return (List<School>) query.getResultList();
	}

	@Override
	@Transactional
	public School getById(int id) {

		Session session = sessionFactory.getCurrentSession();

		return session.get(School.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public School getByEmail(String email) {

		Session session = sessionFactory.getCurrentSession();

		Query<School> query = session.createQuery("from School where email=:email");

		query.setParameter("email", email);

		return (School) query.uniqueResult();
	}

	@Override
	@Transactional
	public List<School> getByName(String name) {
		return null;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public School getSchoolByName(String name) {

		Session session = sessionFactory.getCurrentSession();

		Query<School> query = session.createQuery("from School where name=:name");

		query.setParameter("name", name);

		return (School) query.uniqueResult();
	}

	@Transactional
	public School getSchoolByClassId(int classId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from school where idschool in (" + 
				       "Select school_id from class where idClass = " + classId + ");";

		return (School) session.createSQLQuery(query).addEntity(School.class).uniqueResult();	
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

	@Transactional
	public School getSchoolByTeacherId(int teacherId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from school where idschool in " + 
					   "(Select school_id from teacher where idteacher = " + teacherId + ");";

		return (School) session.createSQLQuery(query).addEntity(School.class).uniqueResult();
	}

	@Transactional
	public School getSchoolByStudentId(int studentId) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from school where idschool in(\r\n" + 
					   "Select school_id from class where idClass in(\r\n" + 
					   "Select class_id from student where idstudent = " + studentId + "))";

		return (School) session.createSQLQuery(query).addEntity(School.class).uniqueResult();
	}

}

















