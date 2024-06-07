package main.repository;

import main.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {


    void saveUser(User user);
    Optional<User> getUserById(int id);
    List<User> getAllUsers();
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);


}
