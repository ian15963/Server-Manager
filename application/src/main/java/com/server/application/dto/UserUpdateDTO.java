package com.server.application.dto;

import com.server.application.service.validation.UserUpdateValid;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO {
	private static final long serialVersionUID = 1L;

	private String password;
	private String matchingPassword;

	public UserUpdateDTO() {
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
