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
        return "users/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<BackendResult> login(@RequestBody HashMap<String, Object> requestMap, HttpSession session) {
        ResponseEntity<BackendResult> response = userService.login(requestMap);

        HashMap<String, Object> resMap = (HashMap<String, Object>) response.getBody().getData();
        boolean login_res = false;
        if(resMap != null && resMap.get("login_res") != null) {
            login_res = (boolean) resMap.get("login_res");
        }

        if(login_res) {
            session.setAttribute("login_res", login_res);
        }

        return response;
    }

    @GetMapping("/menus")
    public ResponseEntity<BackendResult> getUserMenuByRole(HttpSession session) {
        HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");

        return userService.getUserMenuByRole(userMap);
    }

    @GetMapping("/dashboard")
    public String dashboardPage(HttpSession session) {
        String urlPath = "users/dashboard";

        Boolean isLoggedIn = (Boolean) session.getAttribute("login_res");

        if(isLoggedIn == null || !isLoggedIn) {
            urlPath = "redirect:/users/login";
        }

        return urlPath;
    }

}
