package org.ricardo.school_system.daos;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.entities.StudentSubject;
import org.springframework.stereotype.Repository;

@Repository
public class StudentSubjectDao extends GenericDao<StudentSubject> {
	
	@SuppressWarnings("unchecked")
	public StudentSubject getBySubjectIdAndStudentId(int subjectId, int studentId) {
		
		Session session = sessionFactory.getCurrentSession();

		Query<StudentSubject> query = session.createQuery("from StudentSubject where subject_id=:subjectId and student_id=:studentId");

		query.setParameter("subjectId", subjectId);
		query.setParameter("studentId", studentId);

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
