package com.blogproject.springboot.service.impl;

import com.blogproject.springboot.dto.LoginDto;
import com.blogproject.springboot.dto.LoginResponseDto;
import com.blogproject.springboot.dto.RegisterDto;
import com.blogproject.springboot.dto.RegisterResponseDto;
import com.blogproject.springboot.entity.User;
import com.blogproject.springboot.repository.UserRepository;
import com.blogproject.springboot.security.JwtTokenProvider;
import com.blogproject.springboot.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository ourUserRepo;
    private final JwtTokenProvider jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponseDto register(RegisterDto registrationRequest) {
        RegisterResponseDto resp = new RegisterResponseDto();
        try {
            User ourUsers = new User();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            User ourUserResult = ourUserRepo.save(ourUsers);
            if (ourUserResult != null && ourUserResult.getId()>0) {
                resp.setUser(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }
        catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }
        return resp;
    }

    @Override
    public LoginResponseDto login(LoginDto loginRequest) {
        LoginResponseDto response = new LoginResponseDto();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            User user = ourUserRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                    () -> new UsernameNotFoundException("User not found with email "+loginRequest.getEmail())
            );
            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
