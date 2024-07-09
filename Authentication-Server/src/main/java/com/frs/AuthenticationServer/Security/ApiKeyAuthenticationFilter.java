package com.frs.AuthenticationServer.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ApiToken apiToken;

    private RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        int jwtFilterStatus = response.getStatus();

        if (jwtFilterStatus == HttpServletResponse.SC_OK){

            String uri = request.getRequestURI();
            String serviceName = getServiceName(uri,2);

            boolean isAuthenticated = true;
            Map<String, Object> claims = generateClaims(serviceName,isAuthenticated);
            String generatedApiToken = generateApiToken(claims,serviceName);

            if(generatedApiToken.isEmpty()){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                filterChain.doFilter(request, response);
            }else {
                Map<String, String> customHeaders = generateCustomHeaders(generatedApiToken);
                CustomHeaderRequestWrapper requestWrapper = new CustomHeaderRequestWrapper(request, customHeaders);

                filterChain.doFilter(requestWrapper, response);
            }

        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token authentication failed");
        }
    }

    private Map<String, String> generateCustomHeaders(String apiToken){
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("Api-Key", apiToken);
        return customHeaders;
    }

    private String generateApiToken(Map<String, Object> claims, String subject){
        return apiToken.generateToken(claims,subject);
    }

    private Map<String, Object> generateClaims(String serviceName, boolean isAuthenticated){
        Map<String, Object> claims = new HashMap<>();
        claims.put("isAuthenticated",isAuthenticated);
        claims.put("Service-Id", serviceName);
        return claims;
    }

    private String getServiceName(String uri,int index){
        String[] pathComponents = uri.split("/");
        String serviceName = null;
        if (pathComponents.length > index) {
            serviceName = pathComponents[index];
        }
        return serviceName;
    }
}
