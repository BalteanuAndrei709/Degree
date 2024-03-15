package com.example.degree.Utils;

import java.security.SecureRandom;

public class CredentialsGenerator {


    public static Long generateNumber(int size){
        SecureRandom random = new SecureRandom();
        Long code;
        code = Math.abs(random.nextLong() % (long) Math.pow(10, size));
        return code;
    }

    public static String generatePassword() {
        return generateNumber(8).toString();
    }
    public static String generateUsername(String name, String surname){
        name = name.replaceAll("\\s","-").toLowerCase();
        surname = surname.replaceAll("\\s","-").toLowerCase();
        return name + "." + surname + generateNumber(4);

    }
}
