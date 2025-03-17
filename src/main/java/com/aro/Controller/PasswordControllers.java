package com.aro.Controller;

import com.aro.DTO.PasswordsDTO;
import com.aro.Entity.Passwords;
import com.aro.Service.PasswordManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class PasswordControllers {

    private PasswordManagerService passwordManagerService;

    public PasswordControllers(PasswordManagerService passwordManagerService) {
        this.passwordManagerService = passwordManagerService;
    }

    @PostMapping("/addNewPassword")
    public ResponseEntity<String> addNewPassword(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody Passwords passwords) {
        return passwordManagerService.addNewPassword(authHeader, passwords);
    }

    // the error is that we are not able to save the same url in the database
    @PostMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        return passwordManagerService.removePasswordById(id);
    }

    @PostMapping("/findAll")
    public ResponseEntity<List<PasswordsDTO>> findAllPasswords(@RequestHeader("Authorization") String authHeader) {
        return passwordManagerService.getAllPasswordsByUserId(authHeader);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Passwords> updateTheGivenPassword(@PathVariable Long id, @RequestBody Passwords updatePassword) {
        Passwords newSavedPassword = passwordManagerService.updateThePassword(id, updatePassword);

        if (newSavedPassword == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(newSavedPassword);
    }

}
