package org.r2d2c3p0.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class ShowQuery {

	/**
	 * Force map to REMOTEHOSTS. 
	**/
    private static String sql = "SELECT FUNC_NAME FROM REMOTEHOSTS WHERE HOST_NAME = ";

    public static void main(String[] arguments) {
    	
    	String database = arguments[0];
    	String hostName = arguments[1];
		
        try {
        	
        	// JDBC Driver Implementation.
			Class.forName("org.hsqldb.jdbcDriver");
			Connection connection = DriverManager.getConnection("jdbc:hsqldb:" + database, "sa", "");
			// Create connection.
			Statement statement = connection.createStatement();
			// Run SQL command.
	        ResultSet resultSet = statement.executeQuery(sql+"'"+hostName+"'");
	        
	        int iterations=1;
	        ArrayList<String> funcNames = new ArrayList<String>();
	        
	        while (resultSet.next()) {
	        	System.out.println("\t\t"+iterations+". "+resultSet.getString("FUNC_NAME"));
	        	iterations++;
	        	funcNames.add(resultSet.getString("FUNC_NAME"));
	        }
	        
	        @SuppressWarnings("resource")
			Scanner Operation = new Scanner(System.in);
	        
	        System.out.print("\n\tSelect functional ID to use : ");
			String Selection = Operation.next();
			int placeHolder = Integer.parseInt(Selection);
			
			try {
				System.out.print(funcNames.get(--placeHolder));
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				System.err.println("\t\tERROR| selection not found.");
				System.out.print("error occured");
			} catch (IndexOutOfBoundsException ioobe) {
				System.err.println("\t\tERROR| selection not found.");
				System.out.print("error occured");
			}

		} catch (ClassNotFoundException e) {
			System.err.println("\t\tERROR| HSQL driver not found.");
			System.out.print("error occured");
		} catch (SQLException e) {
			System.err.println("\t\tERROR| SQLException occured.");
			System.out.print("error occured");
		}

    }

}
