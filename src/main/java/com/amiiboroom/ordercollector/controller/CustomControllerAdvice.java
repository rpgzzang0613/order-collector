package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.util.enums.ApiMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult> handleException(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        String stackTraceStr = sw.toString();

        log.error("----- Exception StackTrace -----\n{}", stackTraceStr);

        ApiResult apiResult = new ApiResult(ApiMessage.FAILED, null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResult);
    }

}
