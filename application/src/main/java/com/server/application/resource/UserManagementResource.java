package com.server.application.resource;

import com.server.application.dto.UserUpdateDTO;
import com.server.application.model.User;
import com.server.application.repo.UserRepository;
import com.server.application.service.UserService;
import com.server.application.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/management")
public class UserManagementResource {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService service;

    @GetMapping("/confirmUser")
    public String confirmUserToken(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        String verificationToken = verificationTokenService.confirmToken(token);
        response.sendRedirect("http://localhost:3000/login");
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

    @PostMapping("resendVerificationToken")
    public String resendToken(@RequestBody String email, HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/login");
        String verificationToken = verificationTokenService.resendVerificationToken(email);
        return verificationToken;
    }
}
