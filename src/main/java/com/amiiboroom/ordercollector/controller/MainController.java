package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.service.AccountService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final AccountService accountService;

    /**
     * 루트 URL로 진입할때 로그인상태면 대시보드로, 로그아웃상태면 로그인페이지로 리다이렉트
     * @param session
     * @return
     */
    @GetMapping("/")
    public String indexPage(HttpSession session, Model model) {
        String urlPath = "redirect:/accounts/login";

        HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
        if(user != null && !user.isEmpty()) {
            List<HashMap<String, Object>> menuList = accountService.getMenusByRole(user);
            model.addAttribute("menuList", menuList);
            return "redirect:/dashboard";
        }

        return urlPath;
    }

    /**
     * 대시보드 진입할때 로그인 안되어있으면 로그인 페이지로 리다이렉트
     * @param session
     * @return
     */
    @GetMapping("/dashboard")
    public String dashboardPage(HttpSession session, Model model) {
        String urlPath = "dashboard";

        HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");

        if(user == null || user.isEmpty()) {
            urlPath = "redirect:/accounts/login";
        }

        List<HashMap<String, Object>> menuList = accountService.getMenusByRole(user);
        model.addAttribute("menuList", menuList);

        return urlPath;
    }

}
