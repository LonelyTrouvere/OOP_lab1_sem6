package com.stanislav.servlets.action;

import com.stanislav.servlets.db.DAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/connect")
public class MyServlet extends HttpServlet  {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        DAO dao = new DAO();
        Connection conn = dao.getConnection();
        String respo;
        if(conn == null){
            respo = "Nuh uh";
        }else{
            respo = "Bring'em on!";
        }
        PrintWriter out = res.getWriter();
        out.println(respo);
    }
}
