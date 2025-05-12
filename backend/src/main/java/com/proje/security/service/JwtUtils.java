package com.proje.security.service;

import com.proje.security.exceptions.CustomJWTException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;



@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirations}")
    private int jwtExpirations;

    public String getJwtFromHeader(HttpServletRequest request){

        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }





    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateTokenFromUsername(final UserDetails userDetails){

        String username = userDetails.getUsername();

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirations))
                .signWith(key())
                .compact();


    }

    public String generateTokenFromUsername(String username){


        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirations))
                .signWith(key())
                .compact();


    }

    public String getUsernameFromToken(String token){
        return Jwts.parser().verifyWith((SecretKey) key()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String authToken)
    {
        try {

            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);

            return  true;
        }catch(Exception e){

            if(e instanceof MalformedJwtException){

                throw new CustomJWTException("Invalid JWT Token", HttpStatus.UNAUTHORIZED);

            }

            if(e instanceof ExpiredJwtException){

                throw new CustomJWTException("Jwt token is expired", HttpStatus.UNAUTHORIZED);

            }

            if(e instanceof UnsupportedJwtException){

                throw new CustomJWTException("Jwts token is not supported ", HttpStatus.UNAUTHORIZED);

            }

            if(e instanceof  IllegalArgumentException){

                throw new CustomJWTException("Jwt claims string is empty ", HttpStatus.UNAUTHORIZED);

            }
        }

        return  false;
    }

}
