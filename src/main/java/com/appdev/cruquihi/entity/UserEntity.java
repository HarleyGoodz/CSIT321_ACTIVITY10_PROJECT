package com.appdev.cruquihi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String emailAddress;
    private String fullname;
    private String password;
    private String role;

    // Constructors
    public UserEntity() {
        super();
    }

    public UserEntity(String emailAddress, String fullname, String password, String role) {
        super();
        this.emailAddress = emailAddress;
        this.fullname = fullname;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getUserId() { 
        return userId; 
    }
    public void setUserId(Long userId) { 
        this.userId = userId; 
    }

    public String getEmailAddress() { 
        return emailAddress; 
    }
    public void setEmailAddress(String emailAddress) { 
        this.emailAddress = emailAddress; 
    }

    public String getFullname() { 
        return fullname; 
    }
    public void setFullname(String fullname) { 
        this.fullname = fullname; 
    }

    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getRole() { 
        return role; 
    }
    public void setRole(String role) { 
        this.role = role; 
    }

    public void setEmail(String emailAddress2) {
        this.emailAddress = emailAddress2;
    }
}