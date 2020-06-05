package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.entities.Teacher;
import org.springframework.stereotype.Repository;

@Repository
public class TeacherDao extends GenericDao<Teacher> {

	@Override
	public Teacher add(Teacher teacher) {

		Session session = sessionFactory.getCurrentSession();

		session.save(teacher);

		return teacher;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Teacher> getAll() {

		Session session = sessionFactory.getCurrentSession();

		Query<Teacher> query = session.createQuery("from Teacher");

		return (List<Teacher>) query.getResultList();
	}

	@Override
	@Transactional
	public Teacher getById(int id) {

		Session session = sessionFactory.getCurrentSession();

		return session.get(Teacher.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Teacher getByEmail(String email) {

		Session session = sessionFactory.getCurrentSession();

		Query<Teacher> query = session.createQuery("from Teacher where email=:email");

		query.setParameter("email", email);

		return (Teacher) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public Teacher getByEmailAndPassword(LoginForm loginInfo) {

		Session session = sessionFactory.getCurrentSession();

		Query<Teacher> query = session.createQuery("from Teacher where email=:email and password=:password");

		query.setParameter("email", loginInfo.getEmail());
		query.setParameter("password", loginInfo.getPassword());

		return (Teacher) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Teacher> getByName(String name) {

		Session session = sessionFactory.getCurrentSession();

		Query<Teacher> query = session.createQuery("from Teacher where name=:name");

		query.setParameter("name", name);

		return (List<Teacher>) query.getResultList();
	}

	@Override
	public void delete(int id) {

		Session session = sessionFactory.getCurrentSession();

		Teacher teacher = session.get(Teacher.class, id);

		session.delete(teacher);
	}

	@Override
	public Teacher update(Teacher teacher) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(teacher);

		return teacher;
	}

	@SuppressWarnings("unchecked")
	public List<Teacher> getTeachersBySchoolId(int id) {

		Session session = sessionFactory.getCurrentSession();

		Query<Teacher> query = session.createQuery("from Teacher where school_id=:id");

		query.setParameter("id", id);

		return (List<Teacher>) query.getResultList();
	}

}














