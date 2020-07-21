package org.ricardo.school_system.assemblers;

public class DataAndPermissions<UserPermissions, T> {

	private UserPermissions userPermissions;
	private T data;
	
	public DataAndPermissions(UserPermissions userPermissions, T data) {
		this.userPermissions = userPermissions;
		this.data = data;
	}

	public UserPermissions getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(UserPermissions userPermissions) {
		this.userPermissions = userPermissions;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DataAndPermissions [userPermissions=" + userPermissions + ", data=" + data + "]";
	}
	
}
