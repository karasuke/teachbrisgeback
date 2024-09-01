package com.boris.backend.usersapp.backendusersapp.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_comment")
public class UserComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_comment", nullable = false)
    private String userComment;

    @Column(name = "user_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime userDate = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserComment() {
        return userComment;
    }

    public LocalDateTime getUserDate() {
        return userDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public void setUserDate(LocalDateTime userDate) {
        this.userDate = userDate;
    }

    
    
}
