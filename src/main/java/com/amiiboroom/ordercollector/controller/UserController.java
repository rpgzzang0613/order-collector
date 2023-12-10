package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.BackendResult;
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
    public ResponseEntity<ApiResult> signup(@RequestBody HashMap<String, Object> requestMap) {
    public ResponseEntity<BackendResult> signup(@RequestBody HashMap<String, Object> requestMap) {
        return userService.signup(requestMap);
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResult> login(@RequestBody HashMap<String, Object> requestMap, HttpSession session) {
        return userService.login(requestMap);
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<BackendResult> login(@RequestBody HashMap<String, Object> requestMap, HttpSession session) {
        ResponseEntity<BackendResult> response = userService.login(requestMap);

        return response;
    }

    @GetMapping("/menus")
    public ResponseEntity<BackendResult> getUserMenuByRole(HttpSession session) {
        HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");

        return userService.getUserMenuByRole(userMap);
    }

}
