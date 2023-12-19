package com.example.security.security1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "This is not Spring Security's login page.";
    }

    @GetMapping("/users")
    public String user() {
        return "Only users can access.";
    }

    @GetMapping("/admin/users")
    public String admin() {
        return "Only admin can access.";
    }

    @GetMapping("/blog")
    public String info() {
        return "All can access.";
    }

    @GetMapping("/logout")
    public String logout() {
        return "Log out!!!";
    }
}
