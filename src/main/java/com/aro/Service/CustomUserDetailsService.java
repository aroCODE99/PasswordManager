package com.aro.Service;

import com.aro.Entity.Users;
import com.aro.Repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmail(userEmail);
        return user.map(CustomUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException("Users not found"));
    }
}
