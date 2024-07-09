package com.frs.UserMS.Security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class ApiKeyAuthenticationFilter implements Filter {

    @Autowired
    private ApiToken apiToken;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String apiTokenString = request.getHeader("Api-Token");
            System.out.println(apiTokenString);
            if(apiToken.validateToken(apiTokenString)){
                filterChain.doFilter(request,response);
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid API Token");
                // Log the error or relevant information
//            logger.error("Unauthorized request with invalid API Token");
            }
        }catch (RuntimeException e){
            throw new RuntimeException("Invalid API Token");
        }

    }

}