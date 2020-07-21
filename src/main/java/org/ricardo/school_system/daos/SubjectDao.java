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
	public Subject add(Subject subject) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.save(subject);
		
		return subject;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Subject> getAll() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Subject> query = session.createQuery("from Subject");

		return (List<Subject>) query.getResultList();
	}

	@Override
	public Subject getById(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.get(Subject.class, id);
	}
	
	@Transactional
	public Subject getTeacherSubject(int teacherId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Teacher teacher = session.get(Teacher.class, teacherId);
		
		return teacher.getSubject();
	}

	@Override
	public Subject getByEmail(String email) {
		return null;
	}

	@Override
	public List<Subject> getByName(String name) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Subject getSubjectByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Subject> query = session.createQuery("from Student where name=:name");
		
		query.setParameter("name", name);
						
		return (Subject) query.uniqueResult();
	}

	@Override
	public void delete(int id) {
		
		Session session = sessionFactory.getCurrentSession();

		Subject subject = session.get(Subject.class, id);
		
		session.delete(subject);
	}

	@Override
	public Subject update(Subject subject) {
		
		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(subject);
		
		return subject;
	}

	@Transactional
	public Subject getSubjectFromClass(Subject teacherSubject, int classId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from subject s \r\n" + 
				"inner join degree_subject ds on ds.subject_id = s.idsubject\r\n" + 
				"inner join class c on c.degree_id = ds.degree_id\r\n" + 
				"where c.idClass = " + classId + " and s.idsubject = " +  teacherSubject.getId() + ";";
		
		return (Subject) session.createSQLQuery(query).addEntity(Subject.class).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Subject> getSubjectsByClassId(int classId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from subject s \r\n" + 
				"inner join degree_subject ds on ds.subject_id = s.idsubject\r\n" + 
				"inner join class c on c.degree_id = ds.degree_id\r\n" + 
				"where c.idClass = " + classId + ";";
		
		return (List<Subject>) session.createSQLQuery(query).addEntity(Subject.class).getResultList();
	}
	
}














