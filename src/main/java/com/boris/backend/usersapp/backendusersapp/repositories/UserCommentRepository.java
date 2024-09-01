package com.boris.backend.usersapp.backendusersapp.repositories;


import org.springframework.data.repository.CrudRepository;

import com.boris.backend.usersapp.backendusersapp.models.entities.UserComment;

public interface UserCommentRepository extends CrudRepository<UserComment, Long> {




    
}
