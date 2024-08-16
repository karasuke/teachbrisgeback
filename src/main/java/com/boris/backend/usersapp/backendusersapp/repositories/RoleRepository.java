package com.boris.backend.usersapp.backendusersapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.boris.backend.usersapp.backendusersapp.models.entities.Role;

//este repositorio depende de forma directa de la entidad User y de CrudRepository para funcionar ya que es una interfaz que nos permite realizar las operaciones basicas de CRUD

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
