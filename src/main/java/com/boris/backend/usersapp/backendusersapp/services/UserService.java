package com.boris.backend.usersapp.backendusersapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.boris.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.boris.backend.usersapp.backendusersapp.models.entities.User;
import com.boris.backend.usersapp.backendusersapp.models.request.UserRequest;

//Esta interfaz depende de forma directa de la entidad User 

public interface UserService {

    List<UserDto> findAll();

    Page<UserDto> findAll(Pageable pageable);

    Optional<UserDto> findById(@NonNull Long id);

    UserDto save(User user);

    Optional<UserDto> update(UserRequest user, @NonNull Long id);

    void remove(Long id);

}
