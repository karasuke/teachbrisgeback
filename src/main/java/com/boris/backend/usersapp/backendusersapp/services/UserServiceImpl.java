package com.boris.backend.usersapp.backendusersapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boris.backend.usersapp.backendusersapp.models.IUser;
import com.boris.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.boris.backend.usersapp.backendusersapp.models.dto.mapper.DtoMapperUser;
import com.boris.backend.usersapp.backendusersapp.models.entities.Role;
import com.boris.backend.usersapp.backendusersapp.models.entities.User;
import com.boris.backend.usersapp.backendusersapp.models.request.UserRequest;
import com.boris.backend.usersapp.backendusersapp.repositories.RoleRepository;
import com.boris.backend.usersapp.backendusersapp.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {

        List<User> users = (List<User>) repository.findAll();

        return users
                .stream()
                .map(u -> DtoMapperUser
                        .getInstance()
                        .setUser(u)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {

        Page<User> usersPage = repository.findAll(pageable);

        return usersPage.map(u -> DtoMapperUser
                .getInstance()
                .setUser(u)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(@NonNull Long id) {
        return repository
                .findById(id)
                .map(user -> DtoMapperUser
                        .getInstance()
                        .setUser(user)
                        .build());
    }
    /*
     * @Override
     * 
     * @Transactional(readOnly = true)
     * public Optional<UserDto> findById(Long id) {
     * Optional<User> optional = repository.findById(id);
     * if (optional.isPresent()) {
     * return
     * Optional.of(DtoMapperUser.getInstance().setUser(optional.orElseThrow()).build
     * ());
     * }
     * return Optional.empty();
     * 
     * }
     */

    @Override
    @Transactional

    // se guarda el usuario en la base de datos y se encripta la contraseña y se
    // agrega el rol de usuario por defecto ROLE_USER
    public UserDto save(User user) {

        if (user != null) {

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            user.setRoles(getRoles(user));
            return DtoMapperUser.getInstance().setUser(repository.save(user)).build();
        } else {

            throw new IllegalArgumentException("El objeto User no puede ser nulo");
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {
        if (id != null) {
            repository.deleteById(id);
        } else {
            // Manejar el caso en que id es nulo, por ejemplo, lanzar una excepción o
            // realizar alguna lógica específica.
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
    }

    @Override
    @Transactional
    public Optional<UserDto> update(UserRequest user, @NonNull Long id) {
        Optional<User> optional = repository.findById(id);
        // este usuario es de tipo UserDto y se usa para retornar el usuario actualizado
        // en el response del request
        User userOptional = null;

        if (optional.isPresent()) {

            User userDb = optional.orElseThrow();
            userDb.setRoles(getRoles(user));
            userDb = optional.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = repository.save(userDb);

        }
        return Optional.of(DtoMapperUser.getInstance().setUser(userOptional).build());
    }

    private List<Role> getRoles(IUser user) {
        Optional<Role> optional = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        if (optional.isPresent()) {
            roles.add(optional.orElseThrow());
        }
        if (user.isAdmin()) {
            Optional<Role> optionalAdmin = roleRepository.findByName("ROLE_ADMIN");
            if (optionalAdmin.isPresent()) {
                roles.add(optionalAdmin.orElseThrow());
            }
        }
        return roles;
    }

}
