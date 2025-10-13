package org.example;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {

        //Create a Scanner object for taking input from the user
        Scanner scanner = new Scanner(System.in);


        //Prompt user for first name
        System.out.println("Enter your first name:  ");
        String firstName = scanner.nextLine();


        //Prompt user for last name
        System.out.println("Enter your last name:  ");
        String lastName = scanner.nextLine();

        //Prompt user for username with specific requirements
        System.out.println("Enter a username ,but ensure that your username contains an _ and is no more than 5 characters in length : ");
        String username = scanner.nextLine();

        //Prompt user for password with complexity rules
        System.out.println("Enter a password ,please ensure that your password contains at least 8 characters, a capital letter, a number, and a special character:  ");
        String password = scanner.nextLine();

        //Prompt user for phone number (should start with +27)
        System.out.println("Enter your phone number ,it should start with +27 : ");
        String phoneNumber = scanner.nextLine();

        //Create a Login object using the user's details
        Login loginUser = new Login(firstName, lastName, password,phoneNumber,username);

        //Attempt to register the user and display the registration message
        String regMessage = loginUser.registerUser(username, password, phoneNumber);
        System.out.println(regMessage);

        //If registration is successful, proceed to login
        if (regMessage.equals("User is registered successfully.")) {
            System.out.println("Enter your username to login: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter your password to login: ");
            String loginPass = scanner.nextLine();

            // Check login status and display result
            String loginMessage = loginUser.loginStatus(loginUsername, loginPass);
            System.out.println(loginMessage);


                Message.startMessagingApp();


            }

        scanner.close();


    }
}