package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.service.AccountService;
import com.amiiboroom.ordercollector.util.enums.BackendMessage;
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
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * 아이디 존재하는지 확인
     * @param requestMap
     *          - user_id : 가입하려는 아이디
     * @return message : (success / failed)
     *         checkMap
     *           - is_duplicate : 아이디 중복 여부 (true / false)
     */
    @GetMapping("/exists")
    @ResponseBody
    public ResponseEntity<BackendResult> checkAccountExists(@RequestParam HashMap<String, Object> requestMap) {
        return accountService.checkAccountExists(requestMap);
    }

    /**
     * 회원가입 페이지로 진입
     * @return
     */
    @GetMapping("/signup")
    public String signupPage() {
        return "users/signup";
    }

    /**
     * 회원가입 프로세스 진행
     * @param requestMap
     *          - user_id (아이디)
     *          - user_pw (비밀번호)
     *          - email (이메일)
     *          - user_name (이름)
     *          - user_per (권한)
     * @return message (data_save_success / failed)
     */
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<BackendResult> signupProcess(@RequestBody HashMap<String, Object> requestMap) {
        return accountService.signup(requestMap);
    }

    /**
     * 로그인 페이지로 진입
     * @return
     */
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        String urlPath = "users/login";

        HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");

        if(user != null && !user.isEmpty()) {
            urlPath = "redirect:/dashboard";
        }

        return urlPath;
    }

    /**
     * 로그인 프로세스 진행
     * @param requestMap
     *          - user_id
     *          - user_pw
     * @param session
     * @return message : (success / failed)
     *         userMap
     *           - 유저정보 (제거하고 메시지만 내려도 되지않나 싶긴 함)
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<BackendResult> login(@RequestBody HashMap<String, Object> requestMap, HttpSession session) {
        ResponseEntity<BackendResult> response = accountService.login(requestMap);

        BackendMessage message = response.getBody().getMessage();
        if(message == BackendMessage.SUCCESS) {
            // 로그인 성공시 세션에 유저정보 저장
            HashMap<String, Object> user = (HashMap<String, Object>) response.getBody().getData();
            if(user != null && !user.isEmpty()) {
                session.setAttribute("user", user);
            }
        }

        return response;
    }

    /**
     * 이름, 이메일로 아이디 찾기
     * @param requestMap
     *          - user_name
     *          - email
     * @return message : (success / data_not_found)
     *         idMap
     *           - user_id
     */
    @GetMapping("/find-id")
    @ResponseBody
    public ResponseEntity<BackendResult> findUserIdByNameAndEmail(@RequestParam HashMap<String, Object> requestMap) {
        return accountService.findUserIdByNameAndEmail(requestMap);
    }

    /**
     * 패스워드 분실시 임시 비밀번호 발급
     * @param requestMap
     * @return
     */
    @PutMapping("/tmp-pwd")
    @ResponseBody
    public ResponseEntity<BackendResult> updatePwdTemp(@RequestBody HashMap<String, Object> requestMap) {

        return accountService.updatePwdTemp(requestMap);
    }

}
