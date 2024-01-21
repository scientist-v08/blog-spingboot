package com.blogproject.springboot.service;

import com.blogproject.springboot.dto.LoginDto;
import com.blogproject.springboot.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
