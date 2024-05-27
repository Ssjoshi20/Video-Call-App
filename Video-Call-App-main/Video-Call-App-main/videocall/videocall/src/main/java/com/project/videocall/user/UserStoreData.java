package com.project.videocall.user;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserStoreData {

    private static final String DB_URL = "jdbc:mysql://database-1.czygs2w8gzn1.us-east-2.rds.amazonaws.com:3306/users";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "12345678";

    public boolean saveToDatabase(User user){
        createTable();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (username, email, password, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getStatus());
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsersFromDatabase() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT username, email, password, status FROM users";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = User.builder()
                        .username(rs.getString("username"))
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .status(rs.getString("status"))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean userExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateUserStatus(User user) {
        String sql = "UPDATE users SET status = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getStatus());
            stmt.setString(2, user.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ( "
                    + "username VARCHAR(50), "
                    + "email VARCHAR(50), "
                    + "password VARCHAR(50), "
                    + "status VARCHAR(50))";

            statement.executeUpdate(createTableSQL);
            System.out.println("Table created successfully (if it didn't already exist).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
