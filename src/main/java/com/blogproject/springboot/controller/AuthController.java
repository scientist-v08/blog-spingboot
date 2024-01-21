package com.blogproject.springboot.controller;

import com.blogproject.springboot.dto.LoginDto;
import com.blogproject.springboot.dto.RegisterDto;
import com.blogproject.springboot.service.AuthService;
import lombok.AllArgsConstructor;
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
    private AuthService authService;

    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String response = this.authService.login(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = this.authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
