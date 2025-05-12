package com.proje.security.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.proje.security.exceptions.CustomJWTException;
import com.proje.security.service.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public AuthTokenFilter(JwtUtils jwtUtils, @Qualifier("accountDetailService") UserDetailsService userDetailsService, ObjectMapper objectMapper){
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if(jwt != null && jwtUtils.validateToken(jwt)){

                String username = jwtUtils.getUsernameFromToken(jwt);
                UserDetails detail = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(detail, null, detail.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }

            catch (CustomJWTException ex) {


                response.setStatus(ex.getStatus().value());
                response.setContentType("application/json");

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", ex.getMessage());
                errorResponse.put("status", ex.getStatus().value());

                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;


        }
        filterChain.doFilter(request, response);
    }





    private String parseJwt(HttpServletRequest request){

        return jwtUtils.getJwtFromHeader(request);

    }
}
