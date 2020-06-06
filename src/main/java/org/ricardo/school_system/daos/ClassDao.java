package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.entities.Class;

@Repository
public class ClassDao extends GenericDao<Class> {

	@Override
	public Class add(Class schoolClass) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(schoolClass);

		return schoolClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Class> getAll() {

		Session session = sessionFactory.getCurrentSession();

		Query<Class> query = session.createQuery("from Class");

		return (List<Class>) query.getResultList(); 
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public Class getById(int id) {

		Session session = sessionFactory.getCurrentSession();

		Query<Class> query = session.createQuery("from Class where idClass=:id");

		query.setParameter("id", id);

		return (Class) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getByEmail(String email) {

		Session session = sessionFactory.getCurrentSession();

		Query<Class> query = session.createQuery("from Class where email=:email");

		query.setParameter("email", email);

		return (Class) query.uniqueResult();
	}

	@Transactional
	public int getSchoolIdByClassId(int classId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select school_id from class where idClass = " + classId;

		return (int) session.createSQLQuery(query).uniqueResult();
	}

	@Override
	public List<Class> getByName(String name) {
		return null;
	}

	@Override
	public void delete(int id) {

		Session session = sessionFactory.getCurrentSession();

		Class schoolClass = session.get(Class.class, id);

		session.delete(schoolClass);
	}

	@Override
	public Class update(Class schoolClass) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(schoolClass);

		return schoolClass;
	}

	@SuppressWarnings("unchecked")
	public List<Class> getClassesByTeacherId(int id) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from class where idClass in (\r\n" + 
				"Select class_id from teacher_class\r\n" + 
				"where teacher_id = " + id + ");";

		return (List<Class>) session.createSQLQuery(query).addEntity(Class.class).getResultList();
	}
	
	@Transactional
	public Class getClassByStudentId(int id) {
		
		Session session = sessionFactory.getCurrentSession();
	
		String query = "Select * from class where idClass in (" + 
					   "Select class_id from student where idstudent = " + id + ");";
		
		return (Class) session.createSQLQuery(query).addEntity(Class.class).uniqueResult();
	}

	@Transactional
	public Class getClassTeacherRelation(int teacherId, int classId) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from class where idClass in (\r\n" + 
				"Select class_id from teacher_class\r\n" + 
				"where teacher_id = " + teacherId + " and class_id = " + classId + ");";

		return (Class) session.createSQLQuery(query).addEntity(Class.class).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Class> getClassesBySchoolId(int id) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from class where school_id = " + id  + ";";

		return (List<Class>) session.createSQLQuery(query).addEntity(Class.class).getResultList();
	}

}



















