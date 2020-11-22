package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.entities.Class;

@Repository
public class ClassDao extends GenericDao<Class> {

	@SuppressWarnings("unchecked")
	public List<Class> getClassesByTeacherId(int teacherId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from class c\r\n" + 
				"inner join teacher_class tc on tc.class_id = c.idClass\r\n" + 
				"where tc.teacher_id = " + teacherId + ";";

		return (List<Class>) session.createSQLQuery(query).addEntity(Class.class).getResultList();
	}

	@Transactional
	public Class getClassByStudentId(int studentId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from class c\r\n" + 
				"inner join student s on s.class_id = c.idClass\r\n" + 
				"where s.idstudent = " + studentId + ";";

		return (Class) session.createSQLQuery(query).addEntity(Class.class).uniqueResult();
	}

	@Transactional
	public Class getClassTeacherRelation(int teacherId, int classId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from class c \r\n" + 
				"inner join teacher_class tc on tc.class_id = c.idClass\r\n" + 
				"where tc.teacher_id = " + teacherId + " and tc.class_id = " + classId + ";";

		return (Class) session.createSQLQuery(query).addEntity(Class.class).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Class> getClassesBySchoolId(int id) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from class where school_id = " + id  + ";";

		return (List<Class>) session.createSQLQuery(query).addEntity(Class.class).getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Class getClassByClassAndSchoolRelation(int classId, int schoolId) {

		Session session = sessionFactory.getCurrentSession();

		Query<Class> query = session.createQuery("from Class where idClass=:classId and school_id=:schoolId");

		query.setParameter("classId", classId);
		query.setParameter("schoolId", schoolId);

		return (Class) query.uniqueResult();
	}
}
