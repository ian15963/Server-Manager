package com.server.application.auth;

import com.server.application.config.JwtService;
import com.server.application.repo.UserRepository;
import com.server.application.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping
public class AuthenticationController {

    private final AuthenticationService service;

    private final UserRepository repository;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationService service, UserRepository repository, JwtService jwtService) {
        this.service = service;
        this.repository = repository;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/auth")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        var user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var accessTokenCookie = CookieUtil.create(response, "accessToken", jwtToken, false, 600, "localhost");
        var refreshTokenCookie = CookieUtil.create(response, "refreshToken", refreshToken, false, 60000, "localhost");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).body(service.authenticate(request));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var newAccessToken = service.refreshToken(request);
        var accessTokenCookie = CookieUtil.create(response, "accessToken", newAccessToken, false, 600, "localhost");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).build();
    }
}