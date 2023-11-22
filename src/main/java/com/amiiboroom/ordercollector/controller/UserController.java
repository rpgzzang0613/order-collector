package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.dto.users.UserSignupDTO;
import com.amiiboroom.ordercollector.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "LOGIN";
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResult> signup(UserSignupDTO userDTO) {
        return userService.signup(userDTO);
    }

    @GetMapping("/menus")
    public ResponseEntity<ApiResult> getUserMenuByRole(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return userService.getUserMenuByRole(role);
    }

}
