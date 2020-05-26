package org.ricardo.testdb;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		String jdbcUrl = "jdbc:mysql://localhost:3306/school?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";

        String user = "root";
        String password = "slb4ever";

        try {
            System.out.println("Connecting to database: " + jdbcUrl);

            Connection con = DriverManager.getConnection(jdbcUrl, user, password);
            
            System.out.println("Connection successful!");

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}
