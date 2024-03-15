package com.example.degree.Utils;


import com.example.degree.Entity.Account;
import com.example.degree.Entity.Appointment;
import com.example.degree.Entity.ConfirmationToken;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.plaf.PanelUI;
import java.util.Properties;

public class MailSender {

    public static Boolean sendMail(String to, Account account, ConfirmationToken token) {
        String mailMessage = "Thank you for creating an account!\n Here are your credentials:\nUsername:";
        mailMessage += account.getUsername() + "\n" + "Password:" + account.getPassword();
        mailMessage += "\nPlease access the link" + "bellow to activate your account:\n" + "http://localhost:8080/api/registration/confirm?token=" + token.getToken();


        // Sender's email ID needs to be mentioned
        String from = "wellnesswave91@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("wellnesswave91@gmail.com", "cmkwxgyzccqwrftl");

            }

        });

        // Used to debug SMTP issues

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Registration WellnessWave");

            // Now set the actual message
            message.setText(mailMessage);


            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;

        }
    }

    public static Boolean patientCanceled(String patientMail, Appointment appointment) {
        System.out.println(patientMail);

        String mailMessage = "Salut " + AES.decrypt(appointment.getPatient().getName()) + AES.decrypt(appointment.getPatient().getSurname()) + "!\n" + "Prin acest email dorim sa te anuntam ca ai anulat cu succes " + "programarea din data de " + appointment.getDay() + ", de la ora " + appointment.getStartHour() + ", de le medicul" + AES.decrypt(appointment.getMedic().getName()) + AES.decrypt(appointment.getMedic().getSurname()) + ".\n" + "Multumim ca ai ales serviciile noastre de la Wellness Wave!";

        String from = "wellnesswave91@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("wellnesswave91@gmail.com", "cmkwxgyzccqwrftl");

            }

        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(patientMail));

            // Set Subject: header field
            message.setSubject("Registration WellnessWave");

            // Now set the actual message
            message.setText(mailMessage);


            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }

    public static Boolean medicCanceled(String patientMail, Appointment appointment) {
        String mailMessage = "Salut " + AES.decrypt(appointment.getPatient().getName()) + AES.decrypt(appointment.getPatient().getSurname()) + "!\n" + "Prin acest email dorim sa te anuntam ca medicul a anulat " + "programarea din data de " + appointment.getDay() + ", de la ora " + appointment.getStartHour() + ", de le medicul" + AES.decrypt(appointment.getMedic().getName()) + " " +  AES.decrypt(appointment.getMedic().getSurname()) + ".\n" + "Te poti reprograma oricand la o data cand medicul este disponibil" + "\nMultumim ca ai ales serviciile noastre de la Wellness Wave!";

        String from = "wellnesswave91@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("wellnesswave91@gmail.com", "cmkwxgyzccqwrftl");

            }

        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(patientMail));

            // Set Subject: header field
            message.setSubject("Registration WellnessWave");

            // Now set the actual message
            message.setText(mailMessage);


            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }

    public static Boolean resultDone(String patientMail,Appointment appointment){
        String mailMessage = "Salut " + AES.decrypt(appointment.getPatient().getName()) + AES.decrypt(appointment.getPatient().getSurname()) + "!\n" + "Prin acest email dorim sa te anuntam ca resultatele analizelor pentru " + "programarea din data de " + appointment.getDay() + ", de la ora " + appointment.getStartHour() + ", de le medicul" + AES.decrypt(appointment.getMedic().getName()) + " " +  AES.decrypt(appointment.getMedic().getSurname()) + ".\n" + "sunt gata. Pentru a putea vedea resultatele, poti accesa sectiunea documentele mele din contul tau." + "\nMultumim ca ai ales serviciile noastre de la Wellness Wave!";

        String from = "wellnesswave91@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("wellnesswave91@gmail.com", "cmkwxgyzccqwrftl");

            }

        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(patientMail));

            // Set Subject: header field
            message.setSubject("Registration WellnessWave");

            // Now set the actual message
            message.setText(mailMessage);


            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }
}

