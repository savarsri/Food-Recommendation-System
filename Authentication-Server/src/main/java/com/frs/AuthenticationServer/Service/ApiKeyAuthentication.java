package com.frs.AuthenticationServer.Service;

import com.frs.AuthenticationServer.Security.ApiToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiKeyAuthentication {

    @Autowired
    private ApiToken apiToken;

    public ResponseEntity<?> generateToken(HttpServletRequest request){

//        String uri = request.getRequestURI();
        String serviceName = request.getHeader("Service-Id");
//        System.out.println(serviceName);

        boolean isAuthenticated = true;
        Map<String, Object> claims = generateClaims(serviceName,isAuthenticated);
        String generatedApiToken = generateApiToken(claims,serviceName);
//        System.out.println(generatedApiToken);

        if(!generatedApiToken.isEmpty()){
            return new ResponseEntity<>(generatedApiToken,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

//    private Map<String, String> generateCustomHeaders(String apiToken){
//        Map<String, String> customHeaders = new HashMap<>();
//        customHeaders.put("Api-Key", apiToken);
//        return customHeaders;
//    }

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
