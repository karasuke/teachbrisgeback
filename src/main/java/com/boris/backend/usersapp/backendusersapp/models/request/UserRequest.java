package com.boris.backend.usersapp.backendusersapp.models.request;

import com.boris.backend.usersapp.backendusersapp.models.IUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

//esta clase es una entidad de la base de datos y no depende de ninguna otra clase
//se incluye solamente username y email para el registro de usuarios

public class UserRequest implements IUser {
    @NotBlank(message = "Username no puede ser vacio")
    @Size(min = 4, max = 12, message = "Username debe tener entre 4 y 12 caracteres")

    private String username;

    @NotEmpty(message = "Email no puede ser vacio")
    @Email(message = "Email debe ser un correo valido")
    private String email;

    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /* GETTERS AND SETTERS */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
