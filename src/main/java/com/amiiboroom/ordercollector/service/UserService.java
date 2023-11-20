package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.dto.user.MenuDTO;
import com.amiiboroom.ordercollector.util.enums.ApiMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public ResponseEntity<ApiResult> getUserMenuByRole(String role) {
        ApiResult apiResult;
        List<MenuDTO> menuList;

        try {

            // 추후 수정
            menuList = createMenuList(role);

            apiResult = new ApiResult(ApiMessage.SUCCESS, menuList);
        }catch(Exception e) {
            // 로그 저장 후 API 결과 실패로 리턴
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(out);
            e.printStackTrace(printStream);

            String stackTraceStr = out.toString();

            log.error("----- Exception StackTrace -----\n{}", stackTraceStr);

            apiResult = new ApiResult(ApiMessage.FAILED, null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResult);
        }

        return ResponseEntity.ok(apiResult);
    }

    private List<MenuDTO> createMenuList(String role) {
        List<MenuDTO> resultList = new ArrayList<>();

        resultList.add(new MenuDTO("", ""));

        if("ROLE_ADMIN".equals(role)) {
            resultList.add(new MenuDTO("", ""));
        }

        return resultList;
    }

}
