package com.example.degree.Utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {

    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;
    private static final String SECRET_KEY_FILENAME = "secret.key";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final byte[] IV = "1234567890123456".getBytes();

    public static SecretKey generateKey()  {
        try {
            char[] password = "mysecretpassword".toCharArray();
            byte[] salt = new byte[SALT_LENGTH];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            Files.write(Paths.get(SECRET_KEY_FILENAME), secretKey.getEncoded());
            return secretKey;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String input)  {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), new IvParameterSpec(IV));
            byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[IV.length + encrypted.length];
            System.arraycopy(IV, 0, combined, 0, IV.length);
            System.arraycopy(encrypted, 0, combined, IV.length, encrypted.length);
            return Base64.getEncoder().encodeToString(combined);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String input) {
        try {
            byte[] combined = Base64.getDecoder().decode(input);
            System.arraycopy(combined, 0, IV, 0, IV.length);
            byte[] encrypted = new byte[combined.length - IV.length];
            System.arraycopy(combined, IV.length, encrypted, 0, encrypted.length);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new IvParameterSpec(IV));
            byte[] decrypted = cipher.doFinal(encrypted);


            return new String(decrypted, StandardCharsets.UTF_8);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKey getSecretKey() {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(SECRET_KEY_FILENAME));
            return new SecretKeySpec(keyBytes, "AES");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}


