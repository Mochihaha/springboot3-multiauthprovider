package com.multiauthprovider.multiple_authentication_providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Value("${message}")
    String message;

    @GetMapping("/")
    public String protectedRootPage() {
        return message;
    }

    @GetMapping("login")
    public String loginPage() {
        return "Welcome to the Login page that anyone should be able to see without logging in";
    }
}
