package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.entities.Student;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDao extends GenericDao<Student> {

	@Override
	public Student add(Student student) {

		Session session = sessionFactory.getCurrentSession();

		session.save(student);

		return student;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Student> getAll() {

		Session session = sessionFactory.getCurrentSession();

		Query<Student> query = session.createQuery("from Student");

		return (List<Student>) query.getResultList();
	}

	@Override
	@Transactional
	public Student getById(int id) {

		Session session = sessionFactory.getCurrentSession();

		return session.get(Student.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Student getByEmail(String email) {

		Session session = sessionFactory.getCurrentSession();

		Query<Student> query = session.createQuery("from Student where email=:email");

		query.setParameter("email", email);

		return (Student) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public Student getByEmailAndPassword(LoginForm loginInfo) {

		Session session = sessionFactory.getCurrentSession();

		Query<Student> query = session.createQuery("from Student where email=:email and password=:password");

		query.setParameter("email", loginInfo.getEmail());
		query.setParameter("password", loginInfo.getPassword());

		return (Student) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Student> getStudentsByTeacherId(int teacherId) {

		Session session = sessionFactory.getCurrentSession();

		String query = "Select * from student s \r\n" + 
				"inner join teacher_class tc on tc.class_id = s.class_id\r\n" + 
				"inner join teacher t on t.idteacher = tc.teacher_id\r\n" + 
				"where t.idteacher = " + teacherId + "; ";

		return (List<Student>) session.createSQLQuery(query).addEntity(Student.class).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Student> getByName(String name) {

		Session session = sessionFactory.getCurrentSession();

		Query<Student> query = session.createQuery("from Student where name=:name");

		query.setParameter("name", name);

		return (List<Student>) query.getResultList();
	}

	@Override
	public void delete(int id) {

		Session session = sessionFactory.getCurrentSession();

		Student student = session.get(Student.class, id);

		session.delete(student);
	}

	@Override
	public Student update(Student student) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(student);

		return student;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Student getByPhonenumber(int studentPhonenumber) {

		Session session = sessionFactory.getCurrentSession();

		Query<Student> query = session.createQuery("from Student where phonenumber=:phonenumber");

		query.setParameter("phonenumber", studentPhonenumber);

		return (Student) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Student> getStudentsBySchoolId(int schoolId) {

		Session session = sessionFactory.getCurrentSession();
		
		String query = "Select * from student s \r\n" + 
				"inner join class c on s.class_id = c.idClass\r\n" + 
				"inner join school sc on c.school_id = sc.idschool\r\n" + 
				"where sc.idschool = " + schoolId + ";";

		return (List<Student>) session.createSQLQuery(query).addEntity(Student.class).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Student> getStudentsByClassId(int classId){
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Student> query = session.createQuery("from Student where class_id=:classId");

		query.setParameter("classId", classId);

		return (List<Student>) query.getResultList();
	}

	public void expelStudent(int id) {

		Session session = sessionFactory.getCurrentSession();
		
		String query = "Update student set class_id = null where idstudent = " + id;

		 session.createSQLQuery(query).executeUpdate();
	}

}







