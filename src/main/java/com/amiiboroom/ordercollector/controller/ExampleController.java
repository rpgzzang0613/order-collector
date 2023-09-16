package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.service.ExampleService;
import com.amiiboroom.ordercollector.util.OsCheckUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class ExampleController {

    private final ExampleService exampleService;
    private final OsCheckUtil osCheckUtil;

    @GetMapping("/examples")
    public ResponseEntity<ApiResult> findAllExample() {
        return exampleService.findAllExample();
    }

    @GetMapping("/examples/{idx}")
    public ResponseEntity<ApiResult> findOneExample(@PathVariable String idx) {
        return exampleService.findOneExample(idx);
    }

    @GetMapping("/os-type")
    public String getOsType() {
        String example_path = "";

        switch(osCheckUtil.getOsType()) {
            case WINDOWS -> example_path = "윈도우경로를 써야한다";
            case LINUX -> example_path = "리눅스경로를 써야한다";
            case MAC -> example_path = "맥경로를 써야되는데 리눅스랑 비슷하다";
            default -> example_path = "OS 정체가 뭘까";
        }

        return example_path;
    }

    @GetMapping("/naver-orders")
    public ConcurrentHashMap<String, Object> getNaverOrderListFromWeb(@RequestParam String id, @RequestParam String pw) {
        return exampleService.getNaverOrderListFromWeb(id, pw);
    }

}
