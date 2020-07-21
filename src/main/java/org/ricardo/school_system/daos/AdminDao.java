package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.entities.Admin;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao extends GenericDao<Admin> {

	@Override
	public Admin add(Admin admin) {

		Session session = sessionFactory.getCurrentSession();
		
		session.save(admin);
		
		return admin;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Admin> getAll() {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Admin> query = session.createQuery("from Admin");

		return (List<Admin>) query.getResultList();
	}

	@Override
	@Transactional
	public Admin getById(int id) {

		Session session = sessionFactory.getCurrentSession();
		
		return session.get(Admin.class, id);
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public Admin getByEmail(String email) {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Admin> query = session.createQuery("from Admin where email=:email");
		
		query.setParameter("email", email);

		return (Admin) query.uniqueResult();
	}
	
	@Transactional
	public int getSchoolIdByLocalAdminId(int localAdminId) {
		
		Session session = sessionFactory.getCurrentSession();

		String query = "Select school_id from admin where id_admin = " + localAdminId;
		
		return (int) session.createSQLQuery(query).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public Admin getByEmailAndPassword(LoginForm loginInfo) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Admin> query = session.createQuery("from Admin where email=:email and password=:password");
		
		query.setParameter("email", loginInfo.getEmail());
		query.setParameter("password", loginInfo.getPassword());
		
		return (Admin) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Admin> getByName(String name) {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Admin> query = session.createQuery("from Admin where name=:name");
		
		query.setParameter("name", name);
		
		return query.getResultList();
	}

	@Override
	public void delete(int id) {

		Session session = sessionFactory.getCurrentSession();

		Admin admin = session.get(Admin.class, id);
		
		session.delete(admin);
	}

	@Override
	public Admin update(Admin admin) {

		Session session = sessionFactory.getCurrentSession();
		
	    session.saveOrUpdate(admin);
	    
	    return admin;
	}

	@SuppressWarnings("unchecked")
	public List<Admin> getLocalAdmins() {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Admin> query = session.createQuery("from Admin where school_id > 0");
		
		return query.getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Admin getByPhonenumber(int localAdminPhoneNumber) {

		Session session = sessionFactory.getCurrentSession();

		Query<Admin> query = session.createQuery("from Admin where phonenumber=:phonenumber");

		query.setParameter("phonenumber", localAdminPhoneNumber);

		return (Admin) query.uniqueResult();
	}

}













