package com.blogproject.springboot.controller;

import com.blogproject.springboot.dto.LoginDto;
import com.blogproject.springboot.dto.LoginResponseDto;
import com.blogproject.springboot.dto.RegisterDto;
import com.blogproject.springboot.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(token);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
