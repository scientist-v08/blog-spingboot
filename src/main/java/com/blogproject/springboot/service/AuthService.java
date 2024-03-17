package com.blogproject.springboot.service;

import com.blogproject.springboot.dto.LoginDto;
import com.blogproject.springboot.dto.LoginResponseDto;
import com.blogproject.springboot.dto.RegisterDto;
import com.blogproject.springboot.dto.RegisterResponseDto;

public interface AuthService {
    public RegisterResponseDto register(RegisterDto registrationRequest);
    public LoginResponseDto login(LoginDto loginRequest);
}
