package com.boris.backend.usersapp.backendusersapp.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.boris.backend.usersapp.backendusersapp.models.entities.User;
//este repositorio depende de forma directa de la entidad User y de CrudRepository para funcionar ya que es una interfaz que nos permite realizar las operaciones basicas de CRUD

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.username=?1")
    Optional<User> findByUsername2(String username);

    Page<User> findAll(Pageable pageable);

}

