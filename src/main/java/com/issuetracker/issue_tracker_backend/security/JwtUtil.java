package com.issuetracker.issue_tracker_backend.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.security.PublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.jwks.url}")
    private String jwksUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private PublicKey getPublicKey(String kid) throws ParseException {
        try {
            // Fetch JWKS
            JWKSet jwkSet = JWKSet.load(new URL(jwksUrl));
            // Find key by kid
            RSAKey rsaKey = (RSAKey) jwkSet.getKeyByKeyId(kid);
            if (rsaKey == null) {
                throw new RuntimeException("Key not found for kid: " + kid);
            }
            return rsaKey.toPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch JWKS: " + e.getMessage(), e);
        }
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            String kid = Jwts.parser()
                    .build()
                    .parseSignedClaims(token)
                    .getHeader()
                    .get("kid").toString(); // Use get(String, Class) instead of getKeyId()
            PublicKey publicKey = getPublicKey(kid);
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Token validation failed: " + e.getMessage(), e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}