package com.example.ELerning.DOT;

import com.example.ELerning.Entity.User;

public class UserDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private User.Role role;
    private boolean isActive;
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPassword() {
        return password;
    }
    public User.Role getRole() {
        return role;
    }
    public String getUsername() {
        return username;
    }
    public boolean setActive(boolean isActive) {
        this.isActive = isActive;
        return isActive;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(User.Role role) {
        this.role = role;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
