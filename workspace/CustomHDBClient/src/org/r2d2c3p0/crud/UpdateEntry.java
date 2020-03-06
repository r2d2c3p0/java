package org.r2d2c3p0.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class UpdateEntry {

    private static String sql = "UPDATE remotehosts SET host_name=?, func_name=?";

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
            System.out.println("UPDATED: Host "+hostName+" mapped to "+funcName);
        }
        statement.close();
        connection.close();
    }

}
