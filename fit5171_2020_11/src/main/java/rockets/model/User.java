package rockets.model;


import org.apache.commons.lang3.Validate;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;

public class User extends Entity {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        //validate the FirstName can not be null or empty
        notBlank(firstName, "first name cannot be null or empty") ;
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        //validate the LastName can not be null or empty
        notBlank(lastName, "last name cannot be null or empty") ;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        //validate the password can not be null or empty
        notBlank(email, "email cannot be null or empty");
        //validate the email must include "@"
        isEmailValid(email);
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        //validate the password can not be null or empty
        notBlank(password,"password cannot be null or empty");
        //validate the password must have at least 8 characters and at least one capital letter
        isPasswordValid(password);
        this.password = password;
    }

    // match the given password against user's password and return the result
    public boolean isPasswordMatch(String password) {
        return this.password.equals(password.trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static void isPasswordValid(String password) {
        // Validate that password greater or equal than 8 and it must contain at least a Capital letter
        if(password.length()<8 ||password.equals(password.toLowerCase())) {
            Validate.isTrue(false, "Password should not be shorter than 8, and must include at least one Capital letter");
        }
    }

    public static void isEmailValid(String email){
        // Validate that email should contain an @ symbol
        if (!email.contains("@")) {
            Validate.isTrue(false,"email format is wrong");
        }
    }
}
