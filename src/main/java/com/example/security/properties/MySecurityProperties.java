package com.example.security.properties;

import com.example.security.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class MySecurityProperties {

    private String name;
    private String password;
    private UserRole role;

}
