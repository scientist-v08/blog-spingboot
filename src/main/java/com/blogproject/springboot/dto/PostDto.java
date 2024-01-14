package com.blogproject.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    @NotEmpty
    @Size(min = 2, message = "Minimum of 2 characters is required for the title")
    private String title;
    @NotEmpty
    @Size(min = 10, message = "Minimum of 10 characters is required for the description")
    private String description;
    @NotEmpty
    private String content;
}
