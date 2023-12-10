package com.amiiboroom.ordercollector.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(HttpSession session) {
        HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
        if(user == null || user.isEmpty()) {
            return "redirect:/users/login";
        }

        return "index";
    }

}
