package com.stanislav.servlets.db;

import com.stanislav.servlets.model.Book;
import com.stanislav.servlets.model.Password;
import com.stanislav.servlets.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO{
    public void creat(User user, String pass){
        String sql = "INSERT INTO users (id, email, phone, first_name, last_name, password, salt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            String salt = BCrypt.gensalt();
            String hash = BCrypt.hashpw(pass, salt);

            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, hash);
            preparedStatement.setString(7, salt);
            preparedStatement.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public User read(String id){
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return new User(res.getString("id"), res.getString("email"), res.getString("phone"), res.getString("first_name"), res.getString("last_name"));
            }
            return null;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User readByEmai(String email){
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, email);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return new User(res.getString("id"), res.getString("email"), res.getString("phone"), res.getString("first_name"), res.getString("last_name"));
            }
            return null;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Password getPassword(String id){
        String sql = "SELECT password, salt FROM users WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
               return new Password(res.getString("password"), res.getString("salt"));
            }
            return null;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user){
        String sql = "UPDATE users SET first_name = ?, last_name = ?, phone = ? WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }
}
