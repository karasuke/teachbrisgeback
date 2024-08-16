package com.boris.backend.usersapp.backendusersapp.models.dto.mapper;

import com.boris.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.boris.backend.usersapp.backendusersapp.models.entities.User;

public class DtoMapperUser {

    private User user;

    private DtoMapperUser() {
    }

    public static DtoMapperUser getInstance() {
        return new DtoMapperUser();

    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }
    // Se crea un objeto UserDto con los datos del usuario y se retorna el objeto UserDto
    public UserDto build() {
        //si el usuario es nulo se lanza una excepción con el mensaje "Debe pasar el entity user"
        if (user == null) {
            throw new IllegalStateException("Debe pasar el entity user");
        }
        //se crea un objeto UserDto con los datos del usuario y se retorna el objeto UserDto 
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        //se retorna un objeto UserDto con los datos del usuario y si es administrador o no 
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), isAdmin);
    }
}
