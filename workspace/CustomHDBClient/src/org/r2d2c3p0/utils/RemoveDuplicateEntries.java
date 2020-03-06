package org.r2d2c3p0.utils;

import java.sql.*;

public class RemoveDuplicateEntries {

    private static String sql = "SELECT DISTINCT HOST_NAME,FUNC_NAME INTO DUPTABLE FROM remotehosts;\n" +
            "DROP TABLE remotehosts;\n" +
            "ALTER TABLE DUPTABLE RENAME TO remotehosts;" +
            "commit; " +
            "checkpoint;";

    public static void main(String[] args) throws Exception {

        String database = args[0];
        Class.forName("org.hsqldb.jdbcDriver");
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:" + database, "sa", "");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            System.out.println("CHECKPOINT completed for "+database);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}
