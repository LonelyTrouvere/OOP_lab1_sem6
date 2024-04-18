package com.stanislav.servlets.db;


import com.stanislav.servlets.model.Book;
import com.stanislav.servlets.model.GetReservationResponse;
import com.stanislav.servlets.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO extends DAO{
    public void create(Reservation reservation){
        String sql = "INSERT INTO reservation (id, book_id, user_id, date) VALUES (?, ?, ?, ?)";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, reservation.getId());
            preparedStatement.setString(2, reservation.getBookId());
            preparedStatement.setString(3, reservation.getUserId());
            preparedStatement.setLong(4, reservation.getDate());
            preparedStatement.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public List<GetReservationResponse> readMany(String userId){
        String sql = "SELECT reservation.id as res_id, reservation.date as date, book.id as book_id, book.name as name FROM reservation INNER JOIN book ON reservation.book_id = book.id WHERE reservation.user_id = ?";
        try{
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            ResultSet res = preparedStatement.executeQuery();
            ArrayList<GetReservationResponse> reservations = new ArrayList<GetReservationResponse>();
            while (res.next()) {
                reservations.add(new GetReservationResponse(res.getString("res_id"), res.getString("book_id"), res.getString("name"), res.getLong("date")));
            }
            return reservations;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Reservation readSingle(String reservationId){
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, reservationId);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return new Reservation(res.getString("id"), res.getString("user_id"), res.getString("book_id"), res.getLong("date"));
            }
            return null;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(String reservationId){
        String sql = "DELETE FROM reservation WHERE id = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, reservationId);
            preparedStatement.executeQuery();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

}


