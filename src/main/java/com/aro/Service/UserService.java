package com.aro.Service;

import com.aro.Entity.User;
import com.aro.Repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(User user) {
        System.out.println("Adding the user in the database");
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);
        System.out.println("Saved the user to the database successfully");
    }

    public void deleteUserById(Long id) {
        System.out.println("deleting the user in the database with the given id");
        userRepo.deleteById(id);
        System.out.println("Deleted successfully");
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void displayUsers() {
        List<User> users = getAllUsers();

        for (User user: users) {
            System.out.println(user.getEmail());
        }
    }
}
