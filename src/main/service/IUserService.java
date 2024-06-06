package main.service;

import main.model.User;

import java.util.List;

public interface IUserService {


    void saveUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    User getUserByUsername(String username);
    User getUserByEmail(String email);


}
