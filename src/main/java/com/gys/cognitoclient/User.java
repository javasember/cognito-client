package com.gys.cognitoclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    
}
