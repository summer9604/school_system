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
	public School add(School school) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(school);

		return school;
	}

	@Override
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
	public List<School> getByName(String name) {
		return null;
	}

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

	@Override
	public void delete(int id) {

		Session session = sessionFactory.getCurrentSession();

		School school = session.get(School.class, id);

		session.delete(school);
	}

	@Override
	public School update(School school) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(school);

		return school;
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
				"(Select phonenumber from teacher t\r\n" + 
				"where t.phonenumber = " + phonenumber + ")";

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
				"(Select email from teacher t\r\n" + 
				"where t.email = '" + email + "')";

		return (String) session.createSQLQuery(query).uniqueResult();
	}

}

















