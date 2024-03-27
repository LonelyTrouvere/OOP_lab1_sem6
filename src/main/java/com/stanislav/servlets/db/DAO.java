package com.stanislav.servlets.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO{
    private static final String DB_URL = "postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "123456";
    public Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DB_URL, USERNAME, PASSWORD);
            if(connection != null){
                System.out.println("Connected somewhere");
            }else{
                System.out.println("Did not connect");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }



}