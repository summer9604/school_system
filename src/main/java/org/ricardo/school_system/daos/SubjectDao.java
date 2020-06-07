package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
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
		
		Query<Subject> query = session.createQuery("from Subject");

		return (List<Subject>) query.getResultList();
	}

	@Override
	@Transactional
	public Subject getById(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.get(Subject.class, id);
	}
	
	@Transactional
	public Subject getTeacherSubject(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Teacher teacher = session.get(Teacher.class, id);
		
		return teacher.getSubject();
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
	@SuppressWarnings("unchecked")
	public Subject getSubjectByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Subject> query = session.createQuery("from Student where name=:name");
		
		query.setParameter("name", name);
						
		return (Subject) query.uniqueResult();
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

	@Transactional
	public Subject getSubjectFromClass(Subject teacherSubject, int classId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from subject where name = '" + teacherSubject.getName() + "' and idsubject in (\r\n" + 
				"Select subject_id from degree_subject \r\n" + 
				"where degree_id = (Select degree_id from class where idClass = " + classId + "));";
		
		return (Subject) session.createSQLQuery(query).addEntity(Subject.class).uniqueResult();
	}

}
