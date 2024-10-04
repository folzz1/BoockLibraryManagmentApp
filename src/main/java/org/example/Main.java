package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=BoockLibrary";
        String user = "bk";
        String password = "bk";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Добавить книгу");
                System.out.println("2. Показать книги");
                System.out.println("0. Выйти");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 0) {
                    break;
                } else if (choice == 1) {
                    System.out.println("Новая запись:");
                    System.out.println("Введите название книги");
                    String title = scanner.nextLine();
                    System.out.println("Введите автора");
                    String author = scanner.nextLine();
                    System.out.println("Введите год");
                    int year = scanner.nextInt();
                    add(connection, title, author, year);
                } else if (choice == 2) {
                    showBoocks(connection);
                } else if (choice == 3) {

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    static void add(Connection connection, String title, String author, int year) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Boocks (title, author, year) VALUES ( ?, ?, ?)");
        pstmt.setString(1, title);
        pstmt.setString(2, author);
        pstmt.setInt(3, year);
        pstmt.executeUpdate();
        System.out.println("Запись успешно добавлена!");
    }

    static void showBoocks(Connection connection) throws SQLException {
        String selectSql = "SELECT * from Boocks";
        ResultSet resultSet = connection.createStatement().executeQuery(selectSql);

        while (resultSet.next()) {
            System.out.println("ID - " + resultSet.getString(1)
                    + " title - " + resultSet.getString(2)
                    + " author - " + resultSet.getString(3)
                    + " year - " + resultSet.getString(4));
        }
    }
}