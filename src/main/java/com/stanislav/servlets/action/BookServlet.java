package com.stanislav.servlets.action;

import com.google.gson.Gson;
import com.stanislav.servlets.db.BookDAO;
import com.stanislav.servlets.model.Book;
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
import java.util.UUID;

@WebServlet("/book")
public class BookServlet extends HttpServlet  {
    private final BookDAO bookDAO = new BookDAO();
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
            UUID uuid = UUID.randomUUID();
            long copy = (Long) json.get("copy");
            if (copy < 0) {
                throw new IOException("Amount of copies can't be less than 0");
            }
            boolean available = copy != 0;
            bookDAO.create(new Book(
                    uuid.toString(),
                    (String)json.get("name"),
                    copy,
                    available
            ));
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String id = req.getParameter("id");
        Book book = bookDAO.readSingle(id);
        String resString = gson.toJson(book);
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
            Book bookToUpdate = bookDAO.readSingle(id);
            String name = (String) json.get("name");
            Long copy;
            if (json.containsKey("copy")){
                copy = (Long) json.get("copy");
            } else {
                copy = bookToUpdate.getCopy();
            }
            boolean available = copy != 0;
            bookDAO.update(new Book(id, name != null ? name : bookToUpdate.getName(), copy, available));
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }

    public void doDelete (HttpServletRequest req, HttpServletResponse res) throws IOException {
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
            bookDAO.delete(id);
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}
