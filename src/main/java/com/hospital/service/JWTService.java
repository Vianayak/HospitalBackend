package com.hospital.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	private String secretKey = "";

	public JWTService() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", "Admin");
		return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 10 * 60)).and().signWith(getKey()).compact();
	}

	private SecretKey getKey() {
		byte[] keybytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keybytes);
	}

//	validate token
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// Check Token Expiration
	private boolean isTokenExpired(String token) {
		return extractTokenExpiration(token).before(new Date());
	}

//	Extract expiration DateTime of token
	private Date extractTokenExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}

//Extract Username from token
	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	// Extractclaims
	private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

//	ExtractAllClaims
	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
	}

}
