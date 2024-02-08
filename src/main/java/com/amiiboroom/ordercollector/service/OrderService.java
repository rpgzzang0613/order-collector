package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.util.enums.BackendMessage;
import com.amiiboroom.ordercollector.util.enums.SiteType;
import com.amiiboroom.ordercollector.util.webscraping.*;
import com.amiiboroom.ordercollector.util.webscraping.collector.OrderCollector;
import com.amiiboroom.ordercollector.util.webscraping.factory.OrderCollectorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

//    private final TCDao tcDao;

    private final SeleniumUtil seleniumUtil;

    public ResponseEntity<BackendResult> getOrderListFromDB(String site, HashMap<String, Object> requestMap) {
        return null;
    }

    public ResponseEntity<BackendResult> getOrderListFromWeb(String site, HashMap<String, Object> requestMap) {
        BackendResult backendResult;
        WebDriver driver = null;
        SiteType siteType = SiteType.valueOf(site.toUpperCase());
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        try {
            driver = seleniumUtil.initWebDriver();

            OrderCollector orderCollector = OrderCollectorFactory.newInstance(siteType, driver);

            if(orderCollector == null) {
                // 없는 사이트가 파라미터로 넘어오면 리턴 (컨트롤러단에서 한번 거르기때문에 해당하는 경우가 없긴 함)
                backendResult = new BackendResult(BackendMessage.INVALID_REQUEST, null);
                return ResponseEntity.ok(backendResult);
            }

            // 웹사이트 로그인
            boolean isLoggedIn = orderCollector.login(requestMap);

            if(!isLoggedIn) {
                // 로그인 실패시
                backendResult = new BackendResult(BackendMessage.FAILED_TO_SHOP_LOGIN, null);
                return ResponseEntity.ok(backendResult);
            }

            HashMap<String, Object> resultMap = Optional.ofNullable(orderCollector.getOrderList()).orElseGet(HashMap::new);
            if(resultMap.isEmpty()) {
                // 조회결과 없는 경우
                backendResult = new BackendResult(BackendMessage.DATA_NOT_FOUND, null);
                return ResponseEntity.ok(backendResult);
            }

            if(resultMap.get("result") == null || "failure".equals(resultMap.get("result"))) {
                // 페이지 조회 도중 실패한 경우
                backendResult = new BackendResult(BackendMessage.FAILED_TO_CRAWLING, null);
                return ResponseEntity.ok(backendResult);
            }

            resultList = (List<HashMap<String, Object>>) resultMap.get("list");

            if(resultList == null || resultList.isEmpty()) {
                // 리스트가 없는 경우
                backendResult = new BackendResult(BackendMessage.DATA_NOT_FOUND, null);
                return ResponseEntity.ok(backendResult);
            }

        }catch(Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            String stackTraceStr = sw.toString();

            log.error("\n----- Exception StackTrace -----\n{}", stackTraceStr);

            backendResult = new BackendResult(BackendMessage.FAILED, null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(backendResult);
        }finally {
            backendResult = new BackendResult(BackendMessage.SUCCESS, resultList);

            if(driver != null) {
                seleniumUtil.quitWebDriver(driver);
            }
        }

        return ResponseEntity.ok(backendResult);
    }
}
