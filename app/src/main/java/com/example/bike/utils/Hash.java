package com.example.bike.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static String criptografarSenha(String senha) {
        try {
            // Criar instância do MessageDigest com SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Converter a senha em bytes e gerar o hash
            byte[] hash = digest.digest(senha.getBytes());
            
            // Converter o hash em hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
} 