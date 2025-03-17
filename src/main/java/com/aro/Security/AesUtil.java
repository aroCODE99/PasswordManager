package com.aro.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AesUtil {

    private final SecretKey secretKey;

    public AesUtil(@Value("${aes.key}") String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        this.secretKey = new SecretKeySpec(decodedKey, "AES");
    }

    public String encrypt(String password) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedPassword) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] password = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(password);
    }
}
