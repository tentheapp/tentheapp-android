package com.nvcomputers.ten.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by Akash on 7/7/2016.
 */
public class UserValidator {

    private String emailPattern;
    private String usernamePattern;
    private int usernameMinimumLength;
    private int usernameMaximumLength;
    private int passwordMaximumLength;
    private int passwordMinimumLength;
    private String passwordPattern;
    private Activity activity;
    private Context context;

    public UserValidator() {
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    //region Email
    public Boolean validateEmail(String email) {
        validateEmailFormat(email);
        validateEmailIsNotNull(email);
        return true;
    }

    public void validateEmailIsNotNull(String email) {
        if (email == null) {
            // throw new NullEmailException("Email is null");
            showAlert("Email is null");
        }
    }

    public void validateEmailFormat(String email) {
        if (email != null) {
            Pattern emailPattern = Pattern.compile(this.emailPattern);
            boolean hasEmailFormat = emailPattern.matcher(email).matches();
            if (!hasEmailFormat) {
                //throw new InvalidEmailFormatException("Email format is not valid");
                showAlert("Email format is not valid");
            }
        }
    }
    //endregion

    //region Username
    public Boolean validateUsername(String username) {
        validateUsernameHasTheCorrectFormat(username);
        validateUsernameHasMoreThanExpectedCharacters(username);
        validateUsernameHasLessThanExpectedCharacters(username);
        validateUsernameIsNotNull(username);
        return true;
    }

    private void validateUsernameHasTheCorrectFormat(String username) {
        if (username != null) {
            Pattern usernamePattern = Pattern.compile(this.usernamePattern);
            boolean hasUsernameFormat = usernamePattern.matcher(username).matches();
            if (!hasUsernameFormat) {
                //throw new InvalidUsernameFormatException("Username has no correct format");
                showAlert("Username has no correct format");
            }
        }
    }

    private void validateUsernameIsNotNull(String username) {
        if (username == null) {
            //  throw new UsernameIsNullException("Username has no correct format");
            showAlert("Username has no correct format");
        }
    }

    private void validateUsernameHasMoreThanExpectedCharacters(String username) {
        if (username != null && username.length() < this.usernameMinimumLength) {
            //throw new InvalidUsernameLengthException("Username is too short");
            showAlert("Username is too short");
        }
    }

    private void validateUsernameHasLessThanExpectedCharacters(String username) {
        if (username != null && username.length() > this.usernameMaximumLength) {
            // throw new InvalidUsernameLengthException("Username is too long");
            showAlert("Username is too long");
        }
    }

    private void showAlert(String message) {
        if (activity != null)
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region Password
    public Boolean validatePassword(/*String username,*/ String password) {
        validatePassWordHasNotInvalidCharacters(password);
        //validatePasswordIsDifferentFromUsername(username, password);
        validatePasswordHasLesThanExpectedCharacters(password);
        validatePasswordHasMoreThanExpectedCharacters(password);
        validatePasswordNotNull(password);
        return true;
    }

    private void validatePassWordHasNotInvalidCharacters(String password) {
        if (password != null) {
            Pattern passwordPattern = Pattern.compile(this.passwordPattern);
            boolean hasPasswordPattern = passwordPattern.matcher(password).matches();
            if (!hasPasswordPattern) {
                // throw new InvalidPasswordFormatException("Invalid characters in password");
                showAlert("Invalid characters in password");
            }
        }
    }

    public boolean isPasswordValid(String password) {
        return password.length() < 6 || password.length() > 12;
    }

    private void validatePasswordIsDifferentFromUsername(String username, String password) {
        if (username != null && username.equals(password)) {
            //throw new InvalidPasswordException("Password should be different than username");
            showAlert("Password should be different than username");
        }
    }

    private void validatePasswordHasLesThanExpectedCharacters(String password) {
        if (password != null && password.length() > this.passwordMaximumLength) {
            // throw new InvalidPasswordLengthException("Password too long");
            showAlert("Password too long");
        }
    }

    private void validatePasswordHasMoreThanExpectedCharacters(String password) {
        if (password != null && password.length() < this.passwordMinimumLength) {
            //throw new InvalidPasswordLengthException("Password too short");
            showAlert("Password too short");
        }
    }

    private void validatePasswordNotNull(String password) {
        if (password == null) {
            //throw new  NullPasswordException("Password is null");
            showAlert("Password is null");
        }
    }
    //endregion

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public static final String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" +
                ")+";
        private static final String USERNAME_PATTERN = "^([-_A-Za-z0-9])*$";
        private static final int USERNAME_MINIMUM_LENGTH = 3;
        private static final int USERNAME_MAXIMUM_LENGTH = 25;
        private static final int PASSWORD_MINIMUM_LENGTH = 6;
        private static final int PASSWORD_MAXIMUM_LENGTH = 12;
      //  private static final String PASSWORD_PATTERN = "^([A-Za-z0-9_.,&%€@#~])*$";
        private static final String PASSWORD_PATTERN ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,}$";

        public UserValidator validator = new UserValidator();

        public Builder() {
            setDefaults();
        }

        private void setDefaults() {
            validator.emailPattern = EMAIL_PATTERN;
            validator.usernamePattern = USERNAME_PATTERN;
            validator.usernameMinimumLength = USERNAME_MINIMUM_LENGTH;
            validator.usernameMaximumLength = USERNAME_MAXIMUM_LENGTH;
            validator.passwordMinimumLength = PASSWORD_MINIMUM_LENGTH;
            validator.passwordMaximumLength = PASSWORD_MAXIMUM_LENGTH;
            validator.passwordPattern = PASSWORD_PATTERN;
        }

        public Builder emailPattern(String emailPattern) {
            validator.emailPattern = emailPattern;
            return this;
        }

        public Builder usernamePattern(String usernamePattern) {
            validator.usernamePattern = usernamePattern;
            return this;
        }

        public Builder passwordPattern(String passwordPattern) {
            validator.passwordPattern = passwordPattern;
            return this;
        }

        public Builder usernameMinimumLength(Integer usernameMinimumLength) {
            validator.usernameMinimumLength = usernameMinimumLength;
            return this;
        }

        public Builder usernameMaximumLength(Integer usernameMaximumLength) {
            validator.usernameMaximumLength = usernameMaximumLength;
            return this;
        }

        public Builder passwordMinimumLength(Integer passwordMinimumLength) {
            validator.passwordMinimumLength = passwordMinimumLength;
            return this;
        }

        public Builder passwordMaximumLength(Integer passwordMaximumLength) {
            validator.passwordMaximumLength = passwordMaximumLength;
            return this;
        }

        public Builder with(Activity activity) {
            validator.activity = activity;
            return this;
        }

        public UserValidator build() {
            return validator;
        }
    }
}
