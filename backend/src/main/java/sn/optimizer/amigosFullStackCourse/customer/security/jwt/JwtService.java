package sn.optimizer.amigosFullStackCourse.customer.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import sn.optimizer.amigosFullStackCourse.customer.security.permission.Role;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("ALL")
@Service
public class JwtService {

    private static final int VALIDITY_TIME=5;
    private static final String SECRET_KEY="A135Very086Strong753Key1994ForJwt12345678Signature0864213";
    private static final String ISSUER="https://www.optimizer.com";


    public String generateJwtToken(String subject, Map<String, Object>claims){

        return Jwts.builder()
                .subject(subject)
                .issuer(ISSUER)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(2, ChronoUnit.MINUTES)))
                .claims(claims)
                .signWith(getKey())
                .compact();
    }

    public String getSubject(Jws<Claims> claims){
        return getJwtBody(claims).getSubject();
    }

    public Set<? extends GrantedAuthority> getAuthorities(Jws<Claims> claims){
        return getJwtBody(claims).get("authorities", Set.class);
    }

    private Claims getJwtBody(Jws<Claims> claims){
        return claims.getBody();
    }

    public Jws<Claims> parseToken(String token){

        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
    }

    public String generateJwtToken(String subject, Role role){
        return generateJwtToken(subject, Map.of("Role", role));
    }

    public String generateJwtToken(String subject, Set<? extends GrantedAuthority> authorities){
        return generateJwtToken(subject, Map.of("authorities", authorities));
    }

    public String generateJwtToken(String subject){
        return generateJwtToken(subject, Map.of());
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
