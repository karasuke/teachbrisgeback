package com.boris.backend.usersapp.backendusersapp.auth.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.boris.backend.usersapp.backendusersapp.models.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.boris.backend.usersapp.backendusersapp.auth.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    /*
     * se inyecta el authenticationManager para poder autenticar al usuario con el
     * username y password que se envian en el request
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    /*
     * se intenta autenticar al usuario con el username y password que se envian en
     * el request
     */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();

        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    /*
     * Funcion que se ejecuta cuando la autenticacion del usuario ha sido exitosa
     */
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        /*
         * el objeto getprincipal es el resultado de la autenticacion y se
         * obtiene el username del objeto getPrincipal
         */
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
                .getUsername();
        /* Se obtiene la coleccion de roles asignados al usuario autenticado */
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
        /* Se verifica si el usuario es administrador */
        boolean isAdmin = roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        /* se crea un nuevo objeto claims para realizar las reclamaciones */

        Map<String, Object> claims = new HashMap<>();

        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
        /*
         * Se agrega la reclamacion isAdmin al objeto claims isAdmin entrega un valor
         * booleano
         */
        claims.put("isAdmin", isAdmin);
        /*
         * Se crea el token utilizando la biblioteca JJWT se añaden las reclamaciones y
         * se firma con la clave secreta y se establece la fecha de emision y caducidad
         * del token
         */
        String token = Jwts.builder()
                .claims(claims)
                .subject(username).signWith(SECRET_KEY)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)).compact();

        /* Respuesta que el servidor enviara al cliente con el token en el header */
        response.addHeader(HEADER, PREFIX_TOKEN + token);
        /*
         * Se crea un objeto HashMap llamado body donde se agrega el token un mensaje
         * con el nombre de usuario
         */
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("message", String.format("Hola %s, has iniciado sesion con exito", username));
        body.put("username", username);
        /*
         * Se convierte la respuesta del objeto Map en formato JSON y se escribe en el
         * cuerpo de la respuesta HTTP
         */
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

    }

    /*
     * se envia un mensaje de error en caso de que la autenticacion sea fallida
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        String errorMessage;
        if (failed instanceof LockedException) {
            errorMessage = "La cuenta de usuario está bloqueada";
        } else if (failed instanceof DisabledException) {
            errorMessage = "La cuenta de usuario está deshabilitada";
        } else if (failed instanceof AccountExpiredException) {
            errorMessage = "La cuenta de usuario ha expirado";
        } else if (failed instanceof CredentialsExpiredException) {
            errorMessage = "Las credenciales de usuario han expirado";
        } else {
            errorMessage = "Error de autenticacion: username o password incorrecto";
        }
        /*
         * Se crea un objeto HashMap llamado body donde se agrega el mensaje de error
         */
        Map<String, Object> body = new HashMap<>();
        body.put("message", errorMessage);
        /*
         * Se convierte la respuesta del objeto Map en formato JSON y se escribe en el
         * cuerpo de la respuesta HTTP
         */
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
    }
}
