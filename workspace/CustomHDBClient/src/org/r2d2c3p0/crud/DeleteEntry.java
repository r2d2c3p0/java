package org.r2d2c3p0.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeleteEntry {

    private static String sql = "DELETE FROM REMOTEHOSTS WHERE host_name=?";

    public static void main(String[] args) throws Exception {

        String database = args[0];
        String hostName = args[1];
        Class.forName("org.hsqldb.jdbcDriver");
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:" + database, "sa", "");

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, hostName);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("DELETED: Host "+hostName);
        }
        statement.close();
        connection.close();
    }
}