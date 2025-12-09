package com.iremayvaz.model.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // Otomatik bean olarak kaydeder
public class AuthEntryPoint implements AuthenticationEntryPoint { // Unauthenticated request'lerde çalışır
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

        // response'u HTML yerine JSON döner.
        response.setContentType("application/json;charset=UTF-8");
        String body = """
      { "status":401,
        "error":"Unauthorized",
        "message":"Kimlik doğrulaması gerekli veya token geçersiz.",
        "path":"%s"
      }
      """.formatted(request.getRequestURI());
        response.getWriter().write(body);
    }
}
