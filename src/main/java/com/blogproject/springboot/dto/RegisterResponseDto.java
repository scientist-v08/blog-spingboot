package com.blogproject.springboot.dto;

import com.blogproject.springboot.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponseDto {
    private Integer statusCode;
    private String message;
    private User user;
}
