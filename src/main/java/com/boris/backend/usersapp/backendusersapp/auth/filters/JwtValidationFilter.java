package com.boris.backend.usersapp.backendusersapp.auth.filters;

import java.io.IOException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.boris.backend.usersapp.backendusersapp.auth.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.boris.backend.usersapp.backendusersapp.auth.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIX_TOKEN)) {
            String token = header.replace(PREFIX_TOKEN, "");

            try {
                Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();

                Object authoritiesClaims = claims.get("authorities");
                // se trae el username del token para poder autenticar al usuario con el
                // username y password que se envian en el request
                String username = claims.getSubject();
                // Se convierte la estructura de objeto a string y luego a bytes para poder
                // convertirlo a una lista de GrantedAuthority el
                // simplegrantedauthorityjsoncreator hace que se ignore el constructor de la
                // clase simplegrantedauthority y se pueda crear el objeto con el json
                Collection<? extends GrantedAuthority> authorities = Arrays
                        .asList(new ObjectMapper()
                                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                                .readValue(authoritiesClaims
                                        .toString()
                                        .getBytes(),
                                        SimpleGrantedAuthority[].class));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
                        null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);

            } catch (JwtException var11) {
                Map<String, String> body = new HashMap<>();
                body.put("error", var11.getMessage());
                body.put("message", "El token JWT no es valido!");
                response.getWriter().write((new ObjectMapper()).writeValueAsString(body));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
            }

        } else {
            chain.doFilter(request, response);
        }
    }
}
