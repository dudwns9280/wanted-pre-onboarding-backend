package com.example.dashboard.config.jwt;

import com.example.dashboard.exception.CommonException;
import com.example.dashboard.exception.ExceptionEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {

    @Value("${jwt.access-token}")
    private long ACCESS_TOKEN_VALID_PERIOD;
    @Value("${jwt.refresh-token}")
    private long REFRESH_TOKEN_VALID_PERIOD;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.grant-type}")
    private String grantType;

    public JwtToken generateToken(String email) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_VALID_PERIOD);
        final String accessToken = Jwts.builder()
                .setSubject("authorization")
                .claim("email", email)
                .setExpiration(accessTokenExpiresIn)
                .signWith(this.getKey())
                .compact();

        String refreshToken = Jwts.builder()
                .claim("email", email)
                .setExpiration(new Date(now + REFRESH_TOKEN_VALID_PERIOD))
                .signWith(this.getKey())
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean verifyToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.getKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new CommonException(ExceptionEnum.EXPIRED_TOKEN);
        } catch (MalformedJwtException e){
            throw new CommonException(ExceptionEnum.WRONG_TYPE_TOKEN);
        } catch (SignatureException e){
            throw new CommonException(ExceptionEnum.WRONG_TYPE_TOKEN);
        }
    }
    public String getEmail(String token){
        return (String) this.getClaims(token).get("email");
    }

    private Claims getClaims(String token){
         return Jwts.parserBuilder().setSigningKey(this.getKey()).build().parseClaimsJws(token).getBody();
    }


}
