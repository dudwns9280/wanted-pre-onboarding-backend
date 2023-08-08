package com.example.dashboard.exception;

import com.google.gson.JsonObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FilterExceptionHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (CommonException ex){
            this.setExceptionResponse(ex, response);
        }
    }
    private void setExceptionResponse(CommonException ex, HttpServletResponse response){
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex);

        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", String.valueOf(exceptionResponse.getStatus().value()));
            jsonObject.addProperty("message", exceptionResponse.getMessage());

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(exceptionResponse.getStatus().value());
            response.getWriter().write(jsonObject.toString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
