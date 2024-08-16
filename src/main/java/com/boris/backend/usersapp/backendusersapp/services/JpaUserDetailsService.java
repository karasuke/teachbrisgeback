package com.boris.backend.usersapp.backendusersapp.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boris.backend.usersapp.backendusersapp.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional(readOnly = true)

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
         * se busca el usuario en la base de datos por el nombre de usuario
         */
        Optional<com.boris.backend.usersapp.backendusersapp.models.entities.User> userOptional = repository
                .findByUsername2(username);
        /*
         * se valida si el usuario existe en la base de datos y si no existe se lanza
         * una excepcion
         */
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(String.format("username %s no existe", username));
        }

        /*
         * se obtiene el usuario de la base de datos y se convierte en un objeto de tipo
         * User de spring security para que pueda ser manejado por spring security
         */
        com.boris.backend.usersapp.backendusersapp.models.entities.User user = userOptional.orElseThrow();
        /*
         * se obtiene la lista de roles del usuario y se convierte en una lista de
         * GrantedAuthority para que spring security pueda manejarla
         * 
         */
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new User(user.getUsername(), user.getPassword(), true, true, true,
                true, authorities);
    }
}
