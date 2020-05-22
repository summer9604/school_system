package org.ricardo.school_system.services;

import java.util.List;

public abstract class GenericService<T> {

	public abstract T add(T t);

	public abstract List<T> getAll();

	public abstract T getById(int id);

	public abstract T getByEmail(String email);

	public abstract List<T> getByName(String name);

	public abstract void delete(int id);

	public abstract T update(T t);

}
