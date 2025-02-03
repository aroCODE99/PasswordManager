package com.aro.Repositorys;

import com.aro.Entity.Passwords;
import com.aro.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordsRepo extends JpaRepository<Passwords, Long> {

    Passwords findByUrl(String url);

    List<Passwords> findAllByUser(Users user);
}
