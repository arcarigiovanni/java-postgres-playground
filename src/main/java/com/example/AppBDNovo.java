package com.example;

import java.sql.DriverManager;
import java.sql.SQLException;

public class AppBDNovo {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgres.Driver");
            var conn = DriverManager.getConnection("jdbc:postgresql://localhost/postgresql", "gitpod", "");
        } catch (SQLException e) {
            System.err.println("Não foi possível conectar a biblioteca: " +e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Não foi possível conectar ao BD: " +e.getMessage());
        }
        
    }
}
