package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResult> signup(@RequestParam HashMap<String, Object> requestMap) {
        return userService.signup(requestMap);
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResult> login(@RequestParam HashMap<String, Object> requestMap) {
        return userService.login(requestMap);
    }

    @GetMapping("/menus")
    public ResponseEntity<ApiResult> getUserMenuByRole(HttpSession session) {
        HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");

        String permission = (String) userMap.get("permission");
        return userService.getUserMenuByRole(permission);
    }

}