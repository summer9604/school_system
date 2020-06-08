package org.ricardo.school_system.daos;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.entities.StudentSubject;
import org.springframework.stereotype.Repository;

@Repository
public class StudentSubjectDao extends GenericDao<StudentSubject> {

	@Override
	public StudentSubject add(StudentSubject studentSubject) {

		Session session = sessionFactory.getCurrentSession();

		session.save(studentSubject);

		return studentSubject;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StudentSubject> getAll() {

		Session session = sessionFactory.getCurrentSession();

		Query<StudentSubject> query = session.createQuery("from StudentSubject");

		return (List<StudentSubject>) query.getResultList();
	}

	@Override
	public StudentSubject getById(int id) {

		Session session = sessionFactory.getCurrentSession();

		return session.get(StudentSubject.class, id);
	}

	@Override
	public StudentSubject getByEmail(String email) {

		Session session = sessionFactory.getCurrentSession();

		return null;
	}

	@Override
	public List<StudentSubject> getByName(String name) {

		Session session = sessionFactory.getCurrentSession();

		return null;
	}

	@Override
	public void delete(int id) {

		Session session = sessionFactory.getCurrentSession();

		StudentSubject studentSubject = session.get(StudentSubject.class, id);

		session.remove(studentSubject);
	}

	@Override
	public StudentSubject update(StudentSubject studentSubject) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(studentSubject);
		
		return studentSubject;
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public StudentSubject getByStudentIdAndTeacherId(int teacherId, int studentId) {
		
		Session session = sessionFactory.getCurrentSession();

		Query<StudentSubject> query = session.createQuery("from StudentSubject where student_id=:studentId and teacher_id=:teacherId");

		query.setParameter("studentId", studentId);
		query.setParameter("teacherId", teacherId);

		return (StudentSubject) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<StudentSubject> getByStudentId(int studentId) {

		Session session = sessionFactory.getCurrentSession();

		Query<StudentSubject> query = session.createQuery("from StudentSubject where student_id=:studentId");

		query.setParameter("studentId", studentId);

		return (List<StudentSubject>) query.getResultList();
	}

}





















