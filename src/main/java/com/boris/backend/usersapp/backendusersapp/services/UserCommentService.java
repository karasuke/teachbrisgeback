package com.boris.backend.usersapp.backendusersapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boris.backend.usersapp.backendusersapp.models.entities.UserComment;
import com.boris.backend.usersapp.backendusersapp.repositories.UserCommentRepository;

@Service
public class UserCommentService {
    @Autowired
    private UserCommentRepository repository;

    public UserComment save(UserComment userComment) {
        return repository.save(userComment);
    }

    public Iterable<UserComment> findAll() {
        return repository.findAll();
    }
}