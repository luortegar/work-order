package cl.creando.skappserver.common.service;

import cl.creando.skappserver.common.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${started-kit.token.secretKey:404E635266556A586E3272357538782F45412453F4428472B4B6250645367566B5970}")
    private String secretKey ;
    @Value("${started-kit.token.duration.milliseconds:1800000}")
    private String tokenDuration;
    @Value("${started-kit.token.expiration.milliseconds:18000000}")
    private String tokenExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User userDetails) {
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("token_type", "access");
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) {
        long tokenTimeNumber = Long.parseLong(tokenDuration);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setId(userDetails.getUserId().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenTimeNumber))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, String expectedType) {
        try {
            final String username = extractUsername(token);
            final String tokenType = extractClaim(token, claims -> (String) claims.get("token_type"));

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token)
                    && expectedType.equals(tokenType); // asegura que sea access o refresh
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateRefreshToken(User user) {
        long tokenExpiration = Long.parseLong(this.tokenExpiration);
        Map<String, String> extraClaims = new HashMap<>();
        extraClaims.put("token_type", "refresh");
        extraClaims.put("userId", user.getUserId().toString());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
