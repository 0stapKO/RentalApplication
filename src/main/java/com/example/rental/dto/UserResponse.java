package com.example.rental.dto;

import com.example.rental.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;

}
