package org.example;

public class Login {

    //User details
    private String username;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    //Stored (registered) user details for validation
    private  String storeUsername;
    private  String storePassword;
    private  String storePhoneNumber;

    //Constructor to initialize the Login object with user details
    public Login(String firstName, String lastName, String password, String phoneNumber, String username) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }


    //Checks if a username is valid.
    public boolean checkUsername (String username) {

        if (username == null) return false;
        return username.contains("_") && username.length() <= 5 ;
    }

    //Checks if a password is valid.
    public boolean checkPassword (String password) {

        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        if (password.length() <8) return false;

        for (char c: password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecialCharacter = true;
        }
        return hasUppercase && hasNumber && hasSpecialCharacter;


    }
    //Checks if a phone number is valid.
    public boolean checkPhonenumber (String phoneNumber) {

        if (phoneNumber == null ) return false;

        //Strict South African phone number format (+27 followed by exactly 9 digits)
        String southAfricaStrict = "^\\+27\\d{9}$";

        //Generic international phone number format (+countryCode + up to 10 digits)
        String genericInternational = "^\\+\\d{1,3}\\d{1,10}$";

        return phoneNumber.matches(southAfricaStrict) || phoneNumber.matches(genericInternational);
    }

    //Attempts to register a user.
    public String registerUser(String username, String password, String phoneNumber) {
        if (!checkUsername(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an _ and is no more than 5 characters in length.";
        }
        if (!checkPassword(password)) {
            return "Password is not correctly formatted; please ensure that your password contains at least 8 characters, a capital letter, a number, and a special character.";
        }
        if (!checkPhonenumber(phoneNumber)) {
            return "Phone number is incorrectly formatted or does not contain international code." ;
        }

        //Store validated details
        this.storeUsername = this.username;
        this.storePassword = this.password;
        this.storePhoneNumber = this.phoneNumber;

        return "User is registered successfully.";
    }

    //Attempts to log in a user.
    public boolean loginUser (String username, String password) {
        if (storeUsername != null && storeUsername.equals(username) && storePassword.equals(password)) {
            return true;
        }
        return false;
    }


    //Returns a login status message.
    public String loginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + " " + lastName + " it is great having you back.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }


}
