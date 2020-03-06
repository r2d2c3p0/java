package org.r2d2c3p0.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) throws Exception {

        String database = args[0];
        String tableName = args[1];
        Class.forName("org.hsqldb.jdbcDriver");
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:" + database, "sa", "");

        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE "+tableName+" " +
                " (HOST_NAME VARCHAR NOT NULL, " +
                " FUNC_NAME VARCHAR NOT NULL); " +
                "commit;" +
                "checkpoint;";
        statement.executeUpdate(sql);
        System.out.println("Created table "+tableName+" in "+database);
        connection.close();
    }

}
