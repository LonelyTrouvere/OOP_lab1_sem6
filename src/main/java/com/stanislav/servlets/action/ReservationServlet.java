package com.stanislav.servlets.action;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.stanislav.servlets.db.BookDAO;
import com.stanislav.servlets.db.ReservationDAO;
import com.stanislav.servlets.model.Book;
import com.stanislav.servlets.model.GetReservationResponse;
import com.stanislav.servlets.model.Reservation;
import com.stanislav.servlets.model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@WebServlet("/reserve")
public class ReservationServlet extends HttpServlet {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final Gson gson = new Gson();
    private final JSONParser jsonParser = new JSONParser();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = (User)req.getAttribute("user");
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        try {
            JSONObject json = (JSONObject) jsonParser.parse(requestBody);
            UUID uuid = UUID.randomUUID();
            reservationDAO.create(new Reservation(
                    uuid.toString(),
                    user.getId(),
                    (String) json.get("bookId"),
                    (Long) json.get("date")
            ));
            bookDAO.reserve((String) json.get("bookId"));
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        User user = (User)req.getAttribute("user");
        List<GetReservationResponse> reservations = reservationDAO.readMany(user.getId());
        String resString = gson.toJson(reservations);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.println(resString);
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException{
        User user = (User)req.getAttribute("user");
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        try {
            JSONObject json = (JSONObject) jsonParser.parse(requestBody);
            Reservation reservation = reservationDAO.readSingle((String)json.get("id"));
            if (!reservation.getUserId().equals(user.getId())){
                throw new IOException("User id mismatch");
            }
            reservationDAO.delete(reservation.getId());
            bookDAO.dereservation(reservation.getBookId());
        }catch (ParseException e) {
            throw new IOException(e);
        }
    }
}
