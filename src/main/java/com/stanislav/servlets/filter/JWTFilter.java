package com.stanislav.servlets.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stanislav.servlets.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(asyncSupported = true, urlPatterns = { "/reserve" })
public class JWTFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("access-token");
        Algorithm algorithm = Algorithm.HMAC256("baeldung");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Baeldung")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String firstName = decodedJWT.getClaim("firstName").asString();
        String lastName = decodedJWT.getClaim("lastName").asString();
        String id = decodedJWT.getClaim("id").asString();
        String email = decodedJWT.getClaim("email").asString();
        String phone = decodedJWT.getClaim("phone").asString();
        User user = new User(id, email, phone, firstName, lastName);
        request.setAttribute("user", user);
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
