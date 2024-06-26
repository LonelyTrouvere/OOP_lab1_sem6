package com.stanislav.servlets.db;


import com.stanislav.servlets.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO extends DAO{

    public void create(Book book){
        String sql = "INSERT INTO book (id, name, copy, available, description, author) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, book.getId());
            preparedStatement.setString(2, book.getName());
            preparedStatement.setLong(3, book.getCopy());
            preparedStatement.setBoolean(4, book.getAvailable());
            preparedStatement.setString(5, book.getDescription());
            preparedStatement.setString(6, book.getAuthor());
            preparedStatement.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public Book readSingle(String id){
        String sql = "SELECT * FROM book WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return new Book(res.getString("id"), res.getString("name"), Integer.parseInt(res.getString("copy")), res.getBoolean("available"), res.getString("description"), res.getString("author"));
            }
            return null;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Book> readMany(){
        String sql = "SELECT * FROM book";
        try{
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet res = preparedStatement.executeQuery();
            ArrayList<Book> books = new ArrayList<Book>();
            while (res.next()) {
                books.add(new Book(res.getString("id"), res.getString("name"), Integer.parseInt(res.getString("copy")), res.getBoolean("available"), res.getString("description"), res.getString("author")));
            }
            return books;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update (Book book){
        String sql = "UPDATE book SET name = ?, copy = ?, available = ?, description = ? WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, book.getName());
            preparedStatement.setLong(2, book.getCopy());
            preparedStatement.setBoolean(3, book.getAvailable());
            preparedStatement.setString(4, book.getDescription());
            preparedStatement.setString(5, book.getId());
            preparedStatement.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete (String id) throws Error{
        String sql = "DELETE FROM book WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);
            preparedStatement.executeQuery();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void reserve(String bookId){
        Book reservedBook = readSingle(bookId);
        long copies = reservedBook.getCopy();
        Connection connection = getConnection();
        String sql;
        if(copies <= 0){
            throw new Error("Can't reserve this book, no copies left");
        }

        try{
            if(copies == 1) {
                sql = "UPDATE book SET copy = ?, available = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, 0);
                preparedStatement.setBoolean(2, false);
                preparedStatement.setString(3, bookId);
                preparedStatement.executeQuery();
            } else {
                sql = "UPDATE book SET copy = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, copies-1);
                preparedStatement.setString(2, bookId);
                preparedStatement.executeQuery();
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void dereservation(String bookId){
        Book reservedBook = readSingle(bookId);
        String sql = "UPDATE book SET copy = ?, available = ? WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, reservedBook.getCopy()+1);
            preparedStatement.setBoolean(2, true);
            preparedStatement.setString(3, bookId);
            preparedStatement.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

}
