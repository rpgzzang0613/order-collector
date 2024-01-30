package com.amiiboroom.ordercollector.util.selenium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class NaverOrderCollector implements OrderCollector {

    private final WebDriver driver;

    @Override
    public boolean login(HashMap<String, Object> requestMap) throws InterruptedException {
        boolean isLoggedIn = false;

        driver.get("https://nid.naver.com/nidlogin.login");
        Thread.sleep(2000);

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        jsExecutor.executeScript(String.format("document.getElementById('id').value = '%s'", requestMap.get("id").toString()));
        Thread.sleep(2203);
        jsExecutor.executeScript(String.format("document.getElementById('pw').value = '%s'", requestMap.get("pw").toString()));
        Thread.sleep(2725);

        driver.findElement(By.cssSelector("#pw")).sendKeys(Keys.ENTER);

        // 2단계 인증을 위한 대기시간
        Thread.sleep(13000);

        try {
            WebElement button = driver.findElement(By.cssSelector("#account")).findElement(By.cssSelector("[class*='btn_logout']"));
            if(button != null && button.getText().equals("로그아웃")) {
                isLoggedIn = true;
            }
        }catch(NoSuchElementException e) {
            // 못찾으면 그냥 isLoggedIn 값 안바꿔서 실패처리
        }

        return isLoggedIn;
    }

    @Override
    public HashMap<String, Object> getOrderList() throws JsonProcessingException, InterruptedException {
        HashMap<String, Object> resultMap = new HashMap<>();

        List<HashMap<String, Object>> resultList = new ArrayList<>();

        driver.get("https://new-m.pay.naver.com/pcpay?serviceGroup=SHOPPING&page=1");

        Thread.sleep(2000);

        int page = 1;
        int totalPage = 0;

        while(true) {
            HashMap<String, Object> tmpMap = getNaverProductListOnePage(driver, page);

            if(!tmpMap.isEmpty() && "success".equals(tmpMap.get("result"))) {
                if(page == 1) {
                    totalPage = Integer.parseInt(tmpMap.get("totalPage").toString());
                }

                resultList.addAll((List<HashMap<String, Object>>)tmpMap.get("list"));

                if(page >= totalPage) {
                    resultMap.put("result", "success");
                    resultMap.put("list", resultList);
                    break;
                }

                page++;
            }else {
                log.error("네이버 주문 목록 페이지 변경중 오류 발생");
                resultMap.put("result", "failure");
                break;
            }
        }

        return resultMap;
    }

    private HashMap<String, Object> getNaverProductListOnePage(WebDriver driver , int page) throws JsonProcessingException {
        log.info(page + "번째 페이지..");

        HashMap<String, Object> resultMap = new HashMap<>();

        driver.get("https://new-m.pay.naver.com/api/timeline/v2/search?serviceGroup=SHOPPING&from=PC_PAYMENT_HISTORY&page=" + page);

        String jsonStr = driver.findElement(By.cssSelector("pre")).getText();

        ObjectMapper objectMapper = new ObjectMapper();

        HashMap<String, Object> outerMap = objectMapper.readValue(jsonStr, HashMap.class);

        String errMsg = validateJsonAndReturnErrorMsg(outerMap);

        if(!errMsg.isEmpty()) {
            resultMap.put("result", "failure");
            resultMap.put("msg", errMsg);

            return resultMap;
        }

        HashMap<String, Object> innerMap = (HashMap<String, Object>) outerMap.get("result");

        List<HashMap<String, Object>> itemList;
        if(innerMap.get("items") != null) {
            itemList = (List<HashMap<String, Object>>) innerMap.get("items");
        }else {
            itemList = null;
        }

        if(itemList == null || itemList.isEmpty()) {
            String msg = "items가 없음";
            log.error(msg);
            resultMap.put("result", "failure");
            resultMap.put("msg", msg);
            return resultMap;
        }

        List<HashMap<String, Object>> resultList = itemList.stream()
                .filter(item -> "ORDER".equals(item.get("serviceType")))
                .collect(Collectors.toList());

        resultMap.put("result", "success");
        resultMap.put("list", resultList);

        return resultMap;
    }

    private String validateJsonAndReturnErrorMsg(HashMap<String, Object> outerMap) {
        String msg = "";

        if(outerMap.get("code") == null) {
            msg = "JSON 조회 실패1 - code가 없음";
        }else if(!"00".equals(outerMap.get("code"))) {
            msg = "JSON 조회 실패2 - code가 00이 아님";
        }else if(outerMap.get("result") == null) {
            msg = "JSON 조회 실패3 - result가 없음";
        }

        if(!msg.isEmpty()) {
            log.error(msg);

            return msg;
        }

        HashMap<String, Object> innerMap = (HashMap<String, Object>) outerMap.get("result");

        if(innerMap.get("success") == null) {
            msg = "JSON 조회 실패4 - success가 없음";
        }else if(!(boolean) innerMap.get("success")) {
            msg = "JSON 조회 실패5 - success가 true가 아님";
        }

        if(!msg.isEmpty()) {
            log.error(msg);
        }

        return msg;
    }

}
