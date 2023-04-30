package com.server.application.resource;

import com.server.application.dto.UserUpdateDTO;
import com.server.application.model.User;
import com.server.application.service.UserService;
import com.server.application.service.VerificationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
public class UserManagementResource {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserService service;

    @GetMapping("/confirmUser")
    public String confirmUserToken(@RequestParam("token") String token){
        String verificationToken = verificationTokenService.confirmToken(token);
        return verificationToken;
    }

    @PostMapping("/recoveryCode")
    public String recoveryPasswordCode(@RequestBody User user){
        return service.passwordRecoveryCode(user.getEmail());
    }

    @PostMapping("/alterPassword")
    public String changePassword(@RequestBody @Valid UserUpdateDTO user){
        return service.alterPassword(user);
    }


}
