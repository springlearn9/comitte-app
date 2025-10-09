package com.ls.auth.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class AuthUtil {

    public static void main(String[] args) {
        // Generate a secure key for HS512
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        // Encode the key to Base64
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        // Print the Base64-encoded key
        System.out.println("Generated JWT Secret Key: " + base64Key);
    }
}
