package com.blogproject.springboot.service.impl;

import com.blogproject.springboot.dto.LoginDto;
import com.blogproject.springboot.dto.RegisterDto;
import com.blogproject.springboot.entity.Role;
import com.blogproject.springboot.entity.User;
import com.blogproject.springboot.exception.BlogAPIException;
import com.blogproject.springboot.repository.RoleRepository;
import com.blogproject.springboot.repository.UserRepository;
import com.blogproject.springboot.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "User login success";
    }

    @Override
    public String register(RegisterDto registerDto) {
        if(this.userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Username already exists");
        }
        if(this.userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Email already exists");
        }
        Set<Role> roles = new HashSet<>();
        Role userRole = this.roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        User newUser = new User();
        newUser.setName(registerDto.getName());
        newUser.setUsername(registerDto.getUsername());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setRoles(roles);
        this.userRepository.save(newUser);
        return "User registered successfully";
    }
}
