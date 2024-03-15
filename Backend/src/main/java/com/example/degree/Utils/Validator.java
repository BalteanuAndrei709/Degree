package com.example.degree.Utils;

import com.example.degree.Request.RegisterMedicRequest;
import com.example.degree.Request.RegisterPatientRequest;

import java.util.regex.Pattern;

public class Validator {

    public static Boolean isEmailValid(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public static Boolean isSocialIdValid(String socialId) {
        return socialId.length() == 13;
    }

    public static Boolean isSurnameValid(String name) {
        return name.matches("^[a-zA-Z]+([\\s-][a-zA-Z]+)*$");
    }

    public static Boolean isNameValid(String surname) {
        return surname.matches("^[a-zA-Z]+([\\s-][a-zA-Z]+)*$");
    }

    public static Boolean isAgeCorrect(String age) {
        int integerValueOfAge = Integer.parseInt(age);
        return (integerValueOfAge >= 0 && integerValueOfAge <= 150);
    }

    public static Boolean isAgeValid(String age) {
        return age.matches("^[0-9]+$");
    }

    public static Boolean isLicenceIdValid(String licenseId){
        return licenseId.length() == 16;
    }


    public static String validateUserInfo(RegisterPatientRequest user) {
        String response = "";
        if (!isSocialIdValid(user.getSocialId())) {
            response += "Social Id must be of length 13.\n";
        }
        if (!isEmailValid(user.getEmail())) {
            response += "Email is not valid.\n";
        }
        if (!isNameValid(user.getName())) {
            response += "Name is not valid.\n";
        }
        if (!isSurnameValid(user.getSurname())) {
            response += "Surname is not valid!\n";
        }
        if (!isAgeCorrect(user.getAge())) {
            response += "Age must be between 0 and 150\n";
        }
        if (!isAgeValid(user.getAge())) {
            response += "Age is not valid.\n";
        }
        return response;
    }

    public static String validateMedicInfo(RegisterMedicRequest medic) {
        String response = "";
        if (!isLicenceIdValid(medic.getLicenseId())) {
            response += "License Id must be of length 16.\n";
        }
        if (!isEmailValid(medic.getEmail())) {
            response += "Email is not valid.\n";
        }
        if (!isNameValid(medic.getName())) {
            response += "Name is not valid.\n";
        }
        if (!isSurnameValid(medic.getSurname())) {
            response += "Surname is not valid!\n";
        }
        if (!isAgeCorrect(medic.getAge())) {
            response += "Age must be between 0 and 150\n";
        }
        if (!isAgeValid(medic.getAge())) {
            response += "Age is not valid.\n";
        }
        return response;
    }
}
