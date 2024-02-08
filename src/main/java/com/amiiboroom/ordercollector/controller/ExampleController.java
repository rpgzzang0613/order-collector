package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.service.ExampleService;
import com.amiiboroom.ordercollector.util.CustomEncryptUtil;
import com.amiiboroom.ordercollector.util.OsCheckUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/examples")
public class ExampleController {

    private final ExampleService exampleService;
    private final OsCheckUtil osCheckUtil;
    private final CustomEncryptUtil encryptUtil;

    @GetMapping("/os-type")
    public String getOsType() {
        String examplePath = "";

        switch(osCheckUtil.getOsType()) {
            case WINDOWS -> examplePath = "윈도우경로를 써야한다";
            case LINUX -> examplePath = "리눅스경로를 써야한다";
            case MAC -> examplePath = "맥경로를 써야되는데 리눅스랑 비슷하다";
            default -> examplePath = "OS 정체가 뭘까";
        }

        return examplePath;
    }

    @GetMapping("/encrypt-test")
    public HashMap<String, Object> testEncrypt() {

        String original = "String Encrypt Test";
        String encStr = encryptUtil.encrypt(original);
        String decStr = encryptUtil.decrypt(encStr);

        HashMap<String, Object> map = new HashMap<>();
        map.put("original", original);
        map.put("encrypted", encStr);
        map.put("decrypted", decStr);

        return map;
    }

    @GetMapping("/exception-test")
    public ResponseEntity<BackendResult> testException() {
        return exampleService.doException();
    }

    @PostMapping("/naver-orders")
    public ConcurrentHashMap<String, Object> getNaverOrderListFromWeb(@RequestBody HashMap<String, Object> requestMap) {
        return exampleService.getNaverOrderListFromWeb(requestMap);
    }

}
