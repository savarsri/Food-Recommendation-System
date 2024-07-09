package com.frs.UserMS.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class ApiToken {
    public static final int TOKEN_VALIDITY = 60;

    private String SECRET_FRS_USER = "4e9b1a5993a6315d29d9b548932c0f0194288497e00473a5b64c61f16964d3e1";
    private String SERVICE_ID = "user-ms";

    private final SecretKey key_frs_user = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_FRS_USER));


    private Boolean getIsAuthenticatedFromToken(String token) {
        return getClaimFromToken(token, "isAuthenticated", Boolean.class);
    }

    private String getServiceIdFromToken(String token) {
        return getClaimFromToken(token, "Service-Id", String.class);
    }

    // Existing code...

    private <T> T getClaimFromToken(String token, String claimName, Class<T> valueType) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get(claimName, valueType);
    }
    //retrieve expiration date from jwt token
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, "exp", Date.class);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key_frs_user)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //validate token
    public Boolean validateToken(String token) {
        return (!isTokenExpired(token) && validateTokenSignature(token) && validateTokenIdentity(token));
    }

    private Boolean validateTokenIdentity(String token){
        return getIsAuthenticatedFromToken(token) && (getServiceIdFromToken(token).equalsIgnoreCase(SERVICE_ID));
    }

    private Boolean validateTokenSignature(String token) {
        try {
            Jwts.parser().verifyWith(key_frs_user).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }
}
