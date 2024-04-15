package com.stanislav.servlets.action;

import com.google.gson.Gson;
import com.stanislav.servlets.db.BookDAO;
import com.stanislav.servlets.db.UserDAO;
import com.stanislav.servlets.model.Book;
import com.stanislav.servlets.model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();
    private final JSONParser jsonParser = new JSONParser();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        try {
            JSONObject json = (JSONObject) jsonParser.parse(requestBody);
            String email = (String) json.get("email");
            User checkExists = userDAO.readByEmai(email);
            if (checkExists != null){
                throw new IOException("User with this email already exists");
            }
            String uuid = UUID.randomUUID().toString();
            userDAO.creat(
                    new User(
                        uuid,
                            email,
                            (String) json.get("phone"),
                            (String) json.get("firstName"),
                            (String) json.get("lastName")
                    ),
                    (String) json.get("password")
            );
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String id = req.getParameter("id");
        User user = userDAO.read(id);
        String resString = gson.toJson(user);
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.println(resString);
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        try {
            JSONObject json = (JSONObject) jsonParser.parse(requestBody);
            String id = (String) json.get("id");
            if (id == null){
                throw new IOException("Must provide id");
            }
            User userToUpdate = userDAO.read(id);
            String firstName = (String) json.get("firstName");
            String lastName = (String) json.get("lastName");
            String phone = (String) json.get("phone");
            userDAO.update(new User(
                        id,
                        userToUpdate.getEmail(),
                        phone != null ? phone : userToUpdate.getPhone(),
                        firstName != null ? firstName : userToUpdate.getFirstName(),
                        lastName != null ? lastName : userToUpdate.getLastName()
                    ));
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}

