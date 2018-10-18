package com.crud.security.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MySimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        String Msg = "";
        if (e.getClass().isAssignableFrom(BadCredentialsException.class)) {
            Msg = "Invalid username or password";
        } else {
            Msg = "Error - " + e.getMessage();
        }
        httpServletRequest.getSession().setAttribute("msg", Msg);
        httpServletResponse.sendRedirect("/login");
    }
}
