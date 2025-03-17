package com.aro.Service;

import com.aro.Entity.UserRoles;
import com.aro.Entity.Users;
import com.aro.Repositorys.UserRepository;
import com.aro.Repositorys.UserRolesRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepo;

    private PasswordEncoder passwordEncoder;

    private UserRolesRepo userRolesRepo;

    private JwtService jwtService;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder,
                       UserRolesRepo userRolesRepo, JwtService jwtService)
    {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userRolesRepo = userRolesRepo;
        this.jwtService = jwtService;
    }

    @Transactional
    public void addUser(Users user, String userName) {
        System.out.println("Adding the user in the database");
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);
        createUserRole(user, userName);
        System.out.println("Saved the user to the database successfully");
    }

    @Transactional
    public void deleteUserById(Long id) {
        System.out.println("deleting the user in the database with the given id");
        userRepo.deleteById(id);
        System.out.println("Deleted successfully");
    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public void displayUsers() {
        List<Users> users = getAllUsers();

        for (Users user: users) {
            System.out.println(user.getEmail());
        }
    }

    @Transactional
    public void createUserRole(Users user, String roleName) {
        if (user.getUserId() == null) {
            userRepo.save(user);
        }

        // Now create and save the UserRoles entity
        UserRoles userRoles = new UserRoles();
        userRoles.setUser(user);  // this is the foreign key
        userRoles.setRoleName(roleName);

        // Save the UserRoles, and cascading will take care of the rest for related entities
        userRolesRepo.save(userRoles);
    }

    public Users getUserByEmail(String email) {
        Optional<Users> user = userRepo.findByEmail(email);
        return user.orElseThrow(() -> new UsernameNotFoundException("User Not found"));
    }

    public Users getUserById(Long userId) {
        Optional<Users> user = userRepo.findById(userId);

        return user.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}

// I GOT THE SOLUTION WE JUST STORE THE USER_ID  INT THE JWT TOKEN AND DO THE EVERY THING
// now we have the id in the jwt token also now let's see how will we fetched that