package com.frs.ReviewMS.Security;

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

    private String SECRET_FRS_REVIEW = "c1f6790f64a7e3e0b5367dbfed0a91ab0e537d9278d8e6950d91f968e06799ea";
    private String SERVICE_ID = "review-ms";

    private final SecretKey key_frs_review = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_FRS_REVIEW));


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
                .verifyWith(key_frs_review)
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
            Jwts.parser().verifyWith(key_frs_review).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }
}
