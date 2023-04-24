package com.server.application.dto;

import com.server.application.service.validation.PasswordMatches;
import com.server.application.service.validation.UserInsertValid;

@UserInsertValid
@PasswordMatches
public class UserInsertDTO extends UserDTO{

    private String password;
    private String matchingPassword;


    UserInsertDTO(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
