package com.amiiboroom.ordercollector.aspect;

import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.service.AccountService;
import com.amiiboroom.ordercollector.util.enums.BackendMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalControllerAdvice {

    private final AccountService accountService;

    /**
     * 페이지 넘어갈때 공통으로 조회하는 데이터들 Model 객체에 추가
     * @param model
     * @param request
     * @param session
     */
    @ModelAttribute
    public void addCommonAttributes(Model model, HttpServletRequest request, HttpSession session) {
        String acceptStr = Optional.ofNullable(request.getHeader("Accept")).orElse("");
        String requestUri = request.getRequestURI();

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || acceptStr.contains("application/json");
        boolean isError = "/error".equals(requestUri);

        if(!isAjax && !isError) {
            // 비동기 요청이나 에러페이지 요청이 아닐때만

            HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
            if(user != null && !user.isEmpty()) {
                List<HashMap<String, Object>> menuList = accountService.getMenusByRole(user);
                model.addAttribute("menuList", menuList);
            }
        }
    }

    /**
     * 예외처리 분리
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BackendResult> handleException(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        String stackTraceStr = sw.toString();

        log.error("----- Exception StackTrace -----\n{}", stackTraceStr);

        BackendResult backendResult = new BackendResult(BackendMessage.FAILED, null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(backendResult);
    }

}
