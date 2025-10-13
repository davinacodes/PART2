import org.example.Login;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginTest {

    //Test that a valid phone number starting with +27 is accepted
    @Test
    public void testValidPhoneNumber() {
        Login login = new Login("Kyle", "Marowa", "ch&&sec@ke99!", "+27838968976", "kyl_1");
        assertTrue(login.checkPhonenumber("+27838968976"));
    }


    //Test that an invalid phone number (not starting with +27) is rejected
    @Test
    public void testInvalidPhoneNumber() {
        Login login = new Login("Kyle", "Marowa", "ch&&sec@ke99!", "+27838968976", "kyl_1");
        assertFalse(login.checkPhonenumber("08966553"));
    }

    //Test that a valid username (contains _ and <= 5 characters) is accepted
    @Test
    public void testValidUsername() {
        Login login = new Login("Kyle", "Marowa", "ch&&sec@ke99!", "+27838968976", "kyl_1");
        assertTrue(login.checkUsername("kyl_1"));
    }

    //Test that an invalid username (too long or contains invalid characters) is rejected
    @Test
    public void testInvalidUsername() {
        Login login = new Login("Kyle", "Marowa", "ch&&sec@ke99!", "+27838968976", "kyl_1");
        assertFalse(login.checkUsername("kyle!!!!!!"));
    }

    //Test that a strong password meeting all requirements is accepted
    @Test
    public void testValidPassword() {
        String validPassword = "Ch&&sec@ke99!";
        Login login = new Login("Kyle", "Marowa", validPassword, "+27838968976", "kyl_1");
        assertTrue("Password should be accepted", login.checkPassword(validPassword));
    }


    //Test that a weak password ("password") is rejected
    @Test
    public void testInvalidPassword() {
        Login login = new Login("Kyle", "Marowa", "ch&&sec@ke99!", "+27838968976", "kyl_1");
        assertFalse(login.checkPassword("password"));
    }

    //Test successful login when username and password match
    @Test
    public  void testLoginSuccess() {
        Login login = new Login("Kyle", "Marowa", "ch&&sec@ke99!", "+27838968976", "kyl_1");
       Assert.assertFalse(login.loginUser("kyl_1","ch&&sec@ke99!"));
    }

    //Test unsuccessful login when password is incorrect
    @Test
    public void testLoginUnsuccessful() {
        Login login = new Login("Kyle", "Marowa", "ch&&sec@ke99!", "+27838968976", "kyl_1");
        Assert.assertFalse(login.loginUser("kyl_1", "WrongPass"));
    }




}
