package app.core.utilities;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import loginManager.Enum.ClientType;

@Service
public class JwtUtil {

	private String signatureAlgorithm = SignatureAlgorithm.HS256.getJcaName();
	private String encodedSecretKey = "this+is+my+key+and+it+must+be+at+least+256+bits+long";
	private Key decodedSecretKey = new SecretKeySpec(Base64.getDecoder().decode(encodedSecretKey),
			this.signatureAlgorithm);

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userDetails.id);
		claims.put("userType", userDetails.clientType);
		claims.put("userPassword", userDetails.password);
		System.out.println("generateToken email : "+ userDetails.email);
		System.out.println("generateToken password: "+ userDetails.password);
		return createToken(claims, userDetails.email);
	}

	private String createToken(Map<String, Object> claims, String subject) {

		Instant now = Instant.now();

		return Jwts.builder().setClaims(claims)

				.setSubject(subject)

				.setIssuedAt(Date.from(now))

				.setExpiration(Date.from(now.plus(30, ChronoUnit.HOURS)))

				.signWith(this.decodedSecretKey)

				.compact();
	}

	private Claims extractAllClaims(String token) throws ExpiredJwtException {
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(this.decodedSecretKey).build();
		return jwtParser.parseClaimsJws(token).getBody();
	}

	/** returns the JWT subject - in our case the email address */
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}
	public int extractUserId(String token) {
		return (int) extractAllClaims(token).get("userId");
	}

	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	private boolean isTokenExpired(String token) {
		try {
			extractAllClaims(token);
			return false;
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	/**
	 * returns true if the user (email) in the specified token equals the one in the
	 * specified user details and the token is not expired
	 */
	public boolean validateToken(String token, String email) {
		final String username = extractUsername(token);
		System.out.println("email from token: "+username);
		System.out.println("real email: "+email);
		return (username.equals(email) && !isTokenExpired(token));
	}

	public static class UserDetails {
		private int id;
		private String name;
		private String password;
		private String email;
		private ClientType clientType ;
		private String token;

		//for company and customer
		public UserDetails(int id, String email, String password, ClientType clientType,String name) {
			this.id = id;
			this.password=password;
			this.email = email;
			this.clientType = clientType;
			this.name=name;
			this.token=null;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public int getId() {
			return id;
		}

		public String getPassword() {
			return password;
		}

		public String getEmail() {
			return email;
		}

		public ClientType getClientType() {
			return clientType;
		}

		
	}
	
}
