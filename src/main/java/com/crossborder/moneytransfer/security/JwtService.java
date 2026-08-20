package com.crossborder.moneytransfer.security;

import com.crossborder.moneytransfer.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service @RequiredArgsConstructor
/** Generates and verifies signed JWT access tokens. */
public class JwtService {
    private final JwtProperties jwtProperties;
    public String generateToken(String subject, Map<String, ?> claims) {
        Date now = new Date();
        return Jwts.builder().claims(claims).subject(subject).issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.getExpirationMs())).signWith(signingKey()).compact();
    }
    public Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token).getPayload();
    }
    private SecretKey signingKey() { return Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(jwtProperties.getSecret())); }
}
