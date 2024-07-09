package com.project.user.util;

import java.time.Instant;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtUtil {
	private static final String SECRET = "My_Secret_Key"; 
	private static final String ISSUER = "Hotel_Advisor";
	private static final Algorithm algorithm = Algorithm.HMAC512(SECRET);
	private static final JWTVerifier verifier = JWT.require(algorithm)
													.withIssuer(ISSUER)
													.build();
	
	public String generateToken(String username) {
		String token = JWT.create()
							.withIssuer(ISSUER)
							.withClaim("username", username)
							.withIssuedAt(Instant.now())
							.withExpiresAt(Instant.now().plusSeconds(60*60))
							.sign(algorithm);
		return token;
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		String username = null;
		try {
			username = extractUsername(token);
		} catch(Exception ex) {
			return false;
		}
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		DecodedJWT decodedJWT = verifier.verify(token);
		return decodedJWT.getExpiresAtAsInstant().isBefore(Instant.now());
	}

	public String extractUsername(String token) {
		DecodedJWT decodedJWT = verifier.verify(token);
		Claim claim = decodedJWT.getClaim("username");
		return claim.asString();
	}
}
