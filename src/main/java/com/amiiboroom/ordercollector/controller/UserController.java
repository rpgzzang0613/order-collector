package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    @GetMapping("/menus")
    public ResponseEntity<ApiResult> getUserMenuByRole(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return userService.getUserMenuByRole(role);
    }

}
