package org.ricardo.school_system.daos;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.springframework.core.GenericTypeResolver;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericDao<T> {  

	@Autowired
	public SessionFactory sessionFactory;

	private Class<T> genericTypeClass;
	
	private String genericTypeClassName;
	
	@PostConstruct
	@SuppressWarnings("unchecked")
	private void initializeVariables() {
		this.genericTypeClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), GenericDao.class);
		this.genericTypeClassName = genericTypeClass.getSimpleName();
	}
	
	public T add(T t) {

		Session session = sessionFactory.getCurrentSession();

		session.save(t);

		return t;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		
		Session session = sessionFactory.getCurrentSession();

		Query<T> query = session.createQuery("from " + genericTypeClassName);

		return (List<T>) query.getResultList();
	}

	@Transactional
	public T getById(int id) {

		Session session = sessionFactory.getCurrentSession();
		
		T t = session.get(genericTypeClass, id);

		return t;
	}

	@SuppressWarnings("unchecked")
	public T getByEmail(String email) {

		Session session = sessionFactory.getCurrentSession();
		
		Query<T> query = session.createQuery("from " + genericTypeClassName + " where email=:email");

		query.setParameter("email", email);

		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> getByName(String name) {

		Session session = sessionFactory.getCurrentSession();

		Query<T> query = session.createQuery("from " + genericTypeClassName + " where name=:name");

		query.setParameter("name", name);

		return (List<T>) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public void delete(int id) {

		Session session = sessionFactory.getCurrentSession();
		
		T t = (T) session.get(genericTypeClassName, id);
		
		session.delete(t);
	}

	public T update(T t) {

		Session session = sessionFactory.getCurrentSession();

		session.saveOrUpdate(t);
		
		return t;
	}

}







