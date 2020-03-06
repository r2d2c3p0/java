package org.r2d2c3p0.crud;

import java.sql.*;

public class InsertEntry {

    private static String sql = "INSERT INTO remotehosts VALUES(?,?)";

    public static void main(String[] args) throws Exception {

        String database = args[0];
        String hostName = args[1];
        String funcName = args[2];
        Class.forName("org.hsqldb.jdbcDriver");
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:" + database, "sa", "");

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, hostName);
        statement.setString(2, funcName);

        int rowsInserted = statement.executeUpdate();
        Statement statement1 = connection.createStatement();
        String sql = "commit;" +
                "checkpoint;";
        statement1.executeUpdate(sql);

        if (rowsInserted > 0) {
            System.out.println("Host "+hostName+" mapped to "+funcName);
        }

        statement.close();
        connection.close();
    }
}