package org.ricardo.school_system.daos;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.ricardo.school_system.dto.LoginForm;
import org.ricardo.school_system.entities.Admin;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao extends GenericDao<Admin> {
	
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
