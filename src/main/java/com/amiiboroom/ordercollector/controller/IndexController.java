package com.amiiboroom.ordercollector.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(HttpSession session) {
        String urlPath = "redirect:/users/login";

        Boolean isLoggedIn = (Boolean) session.getAttribute("login_res");
        if(isLoggedIn != null && isLoggedIn) {
            return "redirect:/users/dashboard";
        }

        return urlPath;
    }

}
