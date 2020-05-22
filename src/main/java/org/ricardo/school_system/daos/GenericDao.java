package org.ricardo.school_system.daos;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericDao<T> {

	@Autowired
	public SessionFactory sessionFactory;
	
	public abstract T add(T t);
	
	public abstract List<T> getAll();
	
	public abstract T getById(int id);
	
	public abstract T getByEmail(String email);
	
	public abstract List<T> getByName(String name);
	
	public abstract void delete(int id);
	
	public abstract T update(T t);
	
}
