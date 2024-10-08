package org.example;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=BookLibrary";
        String user = "bk";
        String password = "bk";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Добавить книгу");
                System.out.println("2. Показать книги");
                System.out.println("3. Обновление данных о книге по id");
                System.out.println("4. Удаление данных о книге по id");
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
                    showBooks(connection);
                } else if (choice == 3) {
                    updatingBookDataByID(connection);
                } else if (choice == 4) {
                    deletingBookDataByID(connection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    static void add(Connection connection, String title, String author, int year) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Books (title, author, year) VALUES ( ?, ?, ?)");
        pstmt.setString(1, title);
        pstmt.setString(2, author);
        pstmt.setInt(3, year);
        pstmt.executeUpdate();
        System.out.println("Запись успешно добавлена!");
    }

    static void showBooks(Connection connection) throws SQLException {
        String selectSql = "SELECT * from Books";
        ResultSet resultSet = connection.createStatement().executeQuery(selectSql);

        while (resultSet.next()) {
            System.out.println("ID - " + resultSet.getString(1)
                    + " title - " + resultSet.getString(2)
                    + " author - " + resultSet.getString(3)
                    + " year - " + resultSet.getString(4));
        }
    }

    static void updatingBookDataByID(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите id книги");
        int id = scanner.nextInt();
        scanner.nextLine();

        String selectQuery = "SELECT * FROM Books WHERE id = ?";
        PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
        selectStmt.setInt(1, id);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            System.out.println("\nТекущие данные книги:");
            System.out.println("Название: " + rs.getString("title"));
            System.out.println("Автор: " + rs.getString("author"));
            System.out.println("Год: " + rs.getString("year"));

            System.out.println("Введите название:");
            String newTitle = scanner.nextLine();
            System.out.println("Введите автора:");
            String newAuthor = scanner.nextLine();
            System.out.println("Введите год:");
            int newYear = scanner.nextInt();

            String updateQuery = "UPDATE Books SET title = ?, author = ?, year = ? WHERE id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setString(1, newTitle);
            updateStmt.setString(2, newAuthor);
            updateStmt.setInt(3, newYear);
            updateStmt.setInt(4, id);

            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("\nДанные книги успешно обновлены.");
            } else {
                System.out.println("\nОшибка при обновлении данных книги.");
            }
        } else {
            System.out.println("\nКнига с таким ID не найдена.");
        }
    }

    static void deletingBookDataByID(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите id книги");
        int id = scanner.nextInt();
        scanner.nextLine();

        String selectQuery = "SELECT * FROM Books WHERE id = ?";
        PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
        selectStmt.setInt(1, id);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            String delQuery = "DELETE FROM Books WHERE id = ?";
            PreparedStatement delStmt = connection.prepareStatement(delQuery);
            delStmt.setInt(1,id);
            delStmt.executeUpdate();
            System.out.println("Данные удалены.");
        } else {
            System.out.println("\nКнига с таким ID не найдена.");
        }
    }
}