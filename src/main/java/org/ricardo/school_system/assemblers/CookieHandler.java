package org.ricardo.school_system.assemblers;

import javax.servlet.http.Cookie;

public class CookieHandler extends Cookie {

	public CookieHandler(String name, String value) {
		super(name, value);
	}

	@Override
	public String toString() {
		return "Name: " + getName() + "; Value: " + getValue();
	}
	
}
