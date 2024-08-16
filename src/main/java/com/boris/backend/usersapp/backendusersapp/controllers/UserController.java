package com.boris.backend.usersapp.backendusersapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boris.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.boris.backend.usersapp.backendusersapp.models.entities.User;
import com.boris.backend.usersapp.backendusersapp.models.request.UserRequest;
import com.boris.backend.usersapp.backendusersapp.services.UserService;

import jakarta.validation.Valid;
// ESTE CONTROLADOR REST DEPENDE DE userService de user y userRequest

//notacion para indicar que es un controlador rest y que todos los metodos devuelven un json por defecto 
@RestController
// notacion para indicar la ruta base de la api rest
@RequestMapping("/users")
// notacion para indicar que se puede acceder desde cualquier origen
@CrossOrigin(originPatterns = "*")
public class UserController {
    // inyeccion de dependencias de la interfaz de servicio de usuarios
    @Autowired
    private UserService service;

    // notacion para indicar que es un metodo get para obtener todos los usuarios de
    // la base de datos
    @GetMapping
    public List<UserDto> list() {
        return service.findAll();
    }

    @GetMapping("/page/{page}")
    public Page<UserDto> list(@PathVariable Integer page) {

        Pageable pageable = PageRequest.of(page, 3);
        return service.findAll(pageable);
    }

    // notacion para indicar que es un metodo get para obtener un usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable @NonNull Long id) {
        Optional<UserDto> userOptional = service.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    // notacion para indicar que es un metodo post para crear un usuario en la base
    // de datos
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    // notacion para indicar que es un metodo put para actualizar un usuario en la
    // base de datos
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result,
            @PathVariable @NonNull Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<UserDto> optional = service.update(user, id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.notFound().build();
    }

    // notacion para indicar que es un metodo delete para eliminar un usuario en la
    // base de datos
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable @NonNull Long id) {
        Optional<UserDto> optional = service.findById(id);
        if (optional.isPresent()) {
            service.remove(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // metodo para validar los campos de los usuarios y devolver los errores en caso
    // de que existan
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        // se devuelve un bad request con los errores
        return ResponseEntity.badRequest().body(errors);
    }

}
