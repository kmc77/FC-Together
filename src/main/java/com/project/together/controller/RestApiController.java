package com.project.together.controller;

import com.project.together.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserService userService;

    @GetMapping("home")
    public String home() {
        return "<h1>home</h1›";
    }

    @GetMapping("/api/v1/member")
    public String member() {
        return "member";
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}
