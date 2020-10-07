package org.ricardo.school_system.daos;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.entities.School;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolDao extends GenericDao<School> {

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

		String query = "Select * from school s inner join class c\r\n" + 
				"on c.school_id = s.idschool\r\n" + 
				"where c.idClass = " + classId + ";";

		return (School) session.createSQLQuery(query).addEntity(School.class).uniqueResult();	
	}

	@Transactional
	public School getSchoolByTeacherId(int teacherId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from school s \r\n" + 
				"inner join teacher t on t.school_id = s.idschool\r\n" + 
				"where t.idteacher = " + teacherId + ";";

		return (School) session.createSQLQuery(query).addEntity(School.class).uniqueResult();
	}

	@Transactional
	public School getSchoolByStudentId(int studentId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from school s \r\n" + 
				"inner join class c on c.school_id = s.idschool\r\n" + 
				"inner join student st on st.class_id = c.idClass\r\n" + 
				"where st.idstudent = " + studentId + ";";

		return (School) session.createSQLQuery(query).addEntity(School.class).uniqueResult();
	}

	@Transactional
	public School getSchoolByLocalAdminIdAndClassId(int localAdminId, int classId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from school s inner join admin a\r\n" + 
				"on a.school_id = s.idschool inner join class c\r\n" + 
				"on c.school_id = s.idschool\r\n" + 
				"where a.id_admin = " + localAdminId + " and c.idClass = " + classId + ";";

		return (School) session.createSQLQuery(query).addEntity(School.class).uniqueResult();
	}

	@Transactional
	public Integer checkPhoneNumberAvailability(int phonenumber) {

		Session session = sessionFactory.getCurrentSession();

		String query = "(Select phonenumber from teacher t\r\n" + 
				"where t.phonenumber = " + phonenumber + ")\r\n" + 
				"union\r\n" + 
				"(Select phonenumber from student s\r\n" + 
				"where s.phonenumber = " + phonenumber + ")\r\n" + 
				"union\r\n" + 
				"(Select phonenumber from admin a\r\n" + 
				"where a.phonenumber = " + phonenumber + ")";

		return (Integer) session.createSQLQuery(query).uniqueResult();
	}

	@Transactional
	public String checkEmailAvailability(String email) {

		Session session = sessionFactory.getCurrentSession();

		String query = "(Select email from teacher t\r\n" + 
				"where t.email = '" + email + "')\r\n" + 
				"union\r\n" + 
				"(Select email from student s\r\n" + 
				"where s.email = '" + email + "')\r\n" + 
				"union\r\n" + 
				"(Select email from admin a\r\n" + 
				"where a.email = '" + email + "')";

		return (String) session.createSQLQuery(query).uniqueResult();
	}

}
