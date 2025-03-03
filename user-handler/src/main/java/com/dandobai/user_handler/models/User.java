package com.dandobai.user_handler.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Role cannot be empty")
    @Pattern(regexp = "USER|ADMIN", message = "Role must be either USER or ADMIN")
    @Column(nullable = false)
    private String role;

    @NotNull(message = "Birthday cannot be null")
    @Past(message = "Birthday must be a date in the past")
    @Column(nullable = false)
    private LocalDate birthday;

    // Constructors
    public User() {
    }

    public User(String name, String email, String username, String password, String role, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.birthday = birthday;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
