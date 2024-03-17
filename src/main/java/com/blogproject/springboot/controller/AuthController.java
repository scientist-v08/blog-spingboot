package com.blogproject.springboot.controller;

import com.blogproject.springboot.dto.LoginDto;
import com.blogproject.springboot.dto.LoginResponseDto;
import com.blogproject.springboot.dto.RegisterDto;
import com.blogproject.springboot.dto.RegisterResponseDto;
import com.blogproject.springboot.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDto> signUp(@RequestBody RegisterDto signUpRequest){
        return ResponseEntity.ok(authService.register(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDto> signIn(@RequestBody LoginDto signInRequest){
        return ResponseEntity.ok(authService.login(signInRequest));
    }

}
