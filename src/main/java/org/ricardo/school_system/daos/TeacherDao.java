package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.dto.LoginForm;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.springframework.stereotype.Repository;

@Repository
public class TeacherDao extends GenericDao<Teacher> {

	@SuppressWarnings("unchecked")
	public Teacher getByEmailAndPassword(LoginForm loginInfo) {

		Session session = sessionFactory.getCurrentSession();

		Query<Teacher> query = session.createQuery("from Teacher where email=:email and password=:password");

		query.setParameter("email", loginInfo.getEmail());
		query.setParameter("password", loginInfo.getPassword());

		return (Teacher) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Teacher> getTeachersBySchoolId(int id) {

		Session session = sessionFactory.getCurrentSession();

		Query<Teacher> query = session.createQuery("from Teacher where school_id=:id");

		query.setParameter("id", id);

		return (List<Teacher>) query.getResultList();
	}

	@Transactional
	public Teacher getTeacherFromClassBySubjectIdAndClassId(Subject teacherSubject, int classId) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from teacher t \r\n" + 
				"inner join teacher_class tc on tc.teacher_id = idteacher\r\n" + 
				"where subject_id = " + teacherSubject.getId() + " and tc.class_id = " + classId + ";";
		
		return (Teacher) session.createSQLQuery(query).addEntity(Teacher.class).uniqueResult();
	}
	
	@Transactional
	public Teacher getStudentTeacherByStudentIdAndSubjectId(int studentId, int subjectId) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from teacher t \r\n" + 
				"inner join teacher_class tc on tc.teacher_id = t.idteacher\r\n" + 
				"inner join student s on tc.class_id = s.class_id\r\n" + 
				"where s.idstudent = " + studentId + " and t.subject_id = " + subjectId + ";";
		
		return (Teacher) session.createSQLQuery(query).addEntity(Teacher.class).uniqueResult();
	}

}
