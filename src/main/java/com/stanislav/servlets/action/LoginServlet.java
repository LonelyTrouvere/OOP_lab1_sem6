package com.stanislav.servlets.action;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.stanislav.servlets.db.UserDAO;
import com.stanislav.servlets.model.Password;
import com.stanislav.servlets.model.Token;
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final JSONParser jsonParser = new JSONParser();
    private final UserDAO userDAO = new UserDAO();
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
            String typedEmail = (String) json.get("email");
            String typedPassword = (String) json.get("password");
            if (typedPassword == null || typedEmail == null){
                throw new IOException("No email or password provided");
            }
            User user = userDAO.readByEmai(typedEmail);
            if (user == null){
                throw new IOException("Incorrect email or password");
            }

            Password pass = userDAO.getPassword(user.getId());
            String hashedPass = BCrypt.hashpw(typedPassword, pass.getSalt());
            if(!hashedPass.equals(pass.getPassword())){
                throw new IOException("Incorrect email or password");
            }

            Algorithm algorithm = Algorithm.HMAC256("baeldung");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Baeldung")
                    .build();
            String jwt = JWT.create()
                    .withIssuer("Baeldung")
                    .withClaim("id", user.getId())
                    .withClaim("firstName", user.getFirstName())
                    .withClaim("lastName", user.getLastName())
                    .withClaim("email", user.getEmail())
                    .withClaim("phone", user.getPhone())
                    .sign(algorithm);

            Token token = new Token(jwt);
            String resString = gson.toJson(token);
            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.println(resString);
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}
