package com.smartnews.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255,message = "Name must be less than 255 characters ")
    private String name;
    @NotBlank(message = "UserName is required")
    private String userName;
    @NotBlank(message = "Password is required")
    @Size(min =6 ,max = 25,message = "Password must be at least 6 characters and less than 25")
    private String password;

}
