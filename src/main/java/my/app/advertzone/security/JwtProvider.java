package my.app.advertzone.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class JwtProvider {

    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long AMOUNT_OF_MILLISECONDS_VALID = 180000;

    private final Set<String> invalidatedTokens = new HashSet<>();


    public String generate(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);

        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        log.info("token generated!");
        return token;
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
        log.info("Invalidated token: " + token);
    }

    public String getUsername(String token) {
        System.out.println(token);
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            log.info("Valid JWT token");
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        } catch (SignatureException e) {
            log.error("Error with the signature of your token: " + e.getMessage());
        }
        log.info("hello there");
        return false;
    }
}