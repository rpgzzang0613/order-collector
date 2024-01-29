package com.amiiboroom.ordercollector.controller;

import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/shop-orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * DB에 저장된 주문 목록 조회
     * @param site - 조회할 사이트
     * @param requestMap -
     * @return 주문 목록
     */
    @GetMapping("/db/{site}")
    public ResponseEntity<BackendResult> getNaverOrderListFromDB(@PathVariable String site, @RequestParam HashMap<String, Object> requestMap) {
        return orderService.getOrderListFromDB(site, requestMap);
    }

    /**
     * 웹사이트 크롤링을 통한 주문 목록 조회
     * @param site - 조회할 사이트
     * @param requestMap - 조회할 사이트의 id, pw
     * @return 주문 목록
     */
    @GetMapping("/web/{site}")
    public ResponseEntity<BackendResult> getNaverOrderListFromWeb(@PathVariable String site, @RequestParam HashMap<String, Object> requestMap) {
        return orderService.getOrderListFromWeb(site, requestMap);
    }

}
