package com.example.demo.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @NotNull
    @Size(min = 4, max = 30)
    private String username;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(min = 4, max = 60)
    private String password;

    @NotNull
    @Size(min = 4, max = 60)
    private String confirmpassword;
    
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
    
    @NotNull
    @Size(min = 1, max = 20)
    private String role;

}

// public class User {
//     private String username;

//     private String password;

//     private String confirmpassword;
    
//     private String name;
    
//     private String role;
// }
