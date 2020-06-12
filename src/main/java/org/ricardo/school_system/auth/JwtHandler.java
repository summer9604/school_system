package org.ricardo.school_system.auth;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import java.time.temporal.ChronoUnit;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtHandler {

	private final byte[] secret = Base64.getDecoder().decode("+inG/ISPbc4mCGvQKHB/qZ1+7BnSfZupvDUvjDQOBC4=");

	public String generateJwtToken(int userId, int schoolId, String userRole) {

		String token = Jwts.builder()
				.claim("user-id", userId)
				.claim("local-admin-school-id", schoolId)
				.claim("user-permissions", userRole)
				.setIssuedAt(Date.from(Instant.now()))
				.setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.signWith(Keys.hmacShaKeyFor(secret))
				.compact();

		return token;
	}

	public JwtUserPermissions getUserPermissions(String token) {

		Jws<Claims> result;

		try {
			result = Jwts.parser()
					.setSigningKey(Keys.hmacShaKeyFor(secret))
					.parseClaimsJws(token);	
		} catch(Exception ex) {
			return null;
		}
		
		String userPermissions = result.getBody().get("user-permissions", String.class);
		int id = result.getBody().get("user-id", Integer.class);
		int schoolId = result.getBody().get("local-admin-school-id", Integer.class);

		return new JwtUserPermissions(id, userPermissions, schoolId);
	}
}
