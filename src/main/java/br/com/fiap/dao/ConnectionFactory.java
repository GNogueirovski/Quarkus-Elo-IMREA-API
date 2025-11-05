package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection connection;

    public static void closeConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("oracle.jdbc.driver.OracleDriver");
            final String URL = System.getenv("DB_URL");
            final String USERNAME = System.getenv("DB_USER");
            final String PASSWORD = System.getenv("DB_PASSWORD");

            if (URL == null || USERNAME == null || PASSWORD == null) {
                throw new RuntimeException("Variáveis de ambiente do banco não configuradas.");
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Erro nome da classe: " + e.getMessage());
        }
        return connection;
    }
}

