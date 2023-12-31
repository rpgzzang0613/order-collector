package com.amiiboroom.ordercollector.aspect;

import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.util.enums.BackendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@ControllerAdvice
public class ControllerAdviceForException {

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
