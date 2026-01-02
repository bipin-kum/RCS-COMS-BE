package com.tcs.rcs.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final Key key;
	private final String issuer;
	private final long expirationMillis;

	public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.issuer}") String issuer,
			@Value("${jwt.expiration-minutes}") long expMinutes) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		this.issuer = issuer;
		this.expirationMillis = expMinutes * 60_000;
	}

	public String generateToken(String username, List<String> roles) {
		var now = Instant.now();
		return Jwts.builder().setSubject(username).setIssuer(issuer).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plusMillis(expirationMillis))).claim("roles", roles)
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public Jws<Claims> parse(String token) {
		return Jwts.parserBuilder().setSigningKey(key).requireIssuer(issuer).build().parseClaimsJws(token);
	}
}
