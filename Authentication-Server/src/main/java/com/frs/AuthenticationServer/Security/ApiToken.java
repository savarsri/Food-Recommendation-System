package com.frs.AuthenticationServer.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class ApiToken {
    public static final int TOKEN_VALIDITY = 60;
    @Value("${app.api.key.user}")
    private String SECRET_FRS_USER = "4e9b1a5993a6315d29d9b548932c0f0194288497e00473a5b64c61f16964d3e1";
    @Value("${app.api.key.review}")
    private String SECRET_FRS_REVIEW = "c1f6790f64a7e3e0b5367dbfed0a91ab0e537d9278d8e6950d91f968e06799ea";

    @Value("${app.api.key.recommendation}")
    private String SECRET_FRS_RECOMMENDATION = "09a48b13a91772a634dab3b8c7cc418cdffea8602462176ec0343a06d68673f9";
    private final SecretKey key_frs_user = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_FRS_USER));
    private final SecretKey key_frs_review = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_FRS_REVIEW));
    private final SecretKey key_frs_recommendation = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_FRS_RECOMMENDATION));

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
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

    //generate token for user
    public String generateToken(Map<String, Object> claims, String serviceName) {
        return doGenerateToken(claims, serviceName);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return switch (subject) {
            case "user-ms" ->
                    Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
                            .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                            .signWith(key_frs_user).compact();
            case "review-ms" ->
                    Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
                            .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                            .signWith(key_frs_review).compact();
            case "recommendation-ms" ->
                    Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
                            .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                            .signWith(key_frs_recommendation).compact();
            default -> "";
        };
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && validateTokenSignature(token));
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
