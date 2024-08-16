package com.boris.backend.usersapp.backendusersapp.models.entities;

import java.util.List;

import com.boris.backend.usersapp.backendusersapp.models.IUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//esta clase es una entidad de la base de datos y no depende de ninguna otra clase 
//se incluyen todos los campos del usuario para el registro de usuarios

@Entity
@Table(name = "users")
public class User implements IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username no puede ser vacio")
    @Column(unique = true)
    @Size(min = 4, max = 12, message = "Username debe tener entre 4 y 12 caracteres")
    private String username;

    @NotBlank(message = "Password no puede ser vacio")
    private String password;

    @NotBlank(message = "Email no puede ser vacio")
    @Email(message = "Email debe ser un correo valido")
    @Column(unique = true)
    private String email;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = {
            @UniqueConstraint(columnNames = { "user_id", "role_id" }) }

    )
    private List<Role> roles;

    @Transient
    private boolean admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
