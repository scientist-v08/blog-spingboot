package com.blogproject.springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDto {
    private Integer statusCode;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
}
