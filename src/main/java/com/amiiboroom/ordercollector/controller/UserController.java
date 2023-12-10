package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage() {
        return "users/signup";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<BackendResult> signup(@RequestBody HashMap<String, Object> requestMap) {
        return userService.signup(requestMap);
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");

        if(user != null) {
            return "redirect:/";
        }

        return "users/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<BackendResult> login(@RequestBody HashMap<String, Object> requestMap, HttpSession session) {
        ResponseEntity<BackendResult> response = userService.login(requestMap);

        HashMap<String, Object> userMap = (HashMap<String, Object>) response.getBody().getData();

        if(userMap != null) {
            session.setAttribute("user", userMap);
        }

        return response;
    }

    @GetMapping("/menus")
    public ResponseEntity<BackendResult> getUserMenuByRole(HttpSession session) {
        HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");

        return userService.getUserMenuByRole(userMap);
    }

}
