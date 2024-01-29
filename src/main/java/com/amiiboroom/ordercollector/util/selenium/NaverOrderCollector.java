package com.amiiboroom.ordercollector.util.selenium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class NaverOrderCollector implements OrderCollector {

    private final SeleniumUtil seleniumUtil;

    @Override
    public boolean login(HashMap<String, Object> requestMap) {
        boolean isLoggedIn = false;

        WebDriver driver = seleniumUtil.initWebDriver();

        try {

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
        }catch(Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            String stackTraceStr = sw.toString();

            log.error("----- Exception StackTrace -----\n{}", stackTraceStr);
        }finally {
            seleniumUtil.quitWebDriver(driver);
        }

        return isLoggedIn;
    }

    @Override
    public List<HashMap<String, Object>> getOrderList() {
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        WebDriver driver = seleniumUtil.initWebDriver();

        try {

            driver.get("https://new-m.pay.naver.com/pcpay?serviceGroup=SHOPPING&page=1");

            Thread.sleep(2000);

            int page = 1;
            int totalPage = 0;


            while(true) {
                HashMap<String, Object> tmpMap = getNaverProductListOnePage(driver, page);

                if(page == 1) {
                    totalPage = Integer.parseInt(tmpMap.get("totalPage").toString());
                }

                resultList.addAll((List<HashMap<String, Object>>)tmpMap.get("list"));

                if(page >= totalPage) {
                    break;
                }

                page++;
            }

        }catch(Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            String stackTraceStr = sw.toString();

            log.error("----- Exception StackTrace -----\n{}", stackTraceStr);
        }finally {
            seleniumUtil.quitWebDriver(driver);
        }

        return resultList;
    }

    private HashMap<String, Object> getNaverProductListOnePage(WebDriver driver , int page) throws JsonProcessingException {
        log.info(page + "번째 페이지..");

        HashMap<String, Object> resultMap = new HashMap<>();

        driver.get("https://new-m.pay.naver.com/api/timeline/v2/search?serviceGroup=SHOPPING&from=PC_PAYMENT_HISTORY&page=" + page);

        String jsonStr = driver.findElement(By.cssSelector("pre")).getText();

        ObjectMapper objectMapper = new ObjectMapper();

        HashMap<String, Object> jsonMap = objectMapper.readValue(jsonStr, HashMap.class);

        if(jsonMap.get("code") == null) {
            log.error("JSON 조회 실패1 - code가 없음");
            resultMap.put("result", "failure");
            return resultMap;
        }

        if(!"00".equals(jsonMap.get("code"))) {
            log.error("JSON 조회 실패2 - code가 00이 아님");
            resultMap.put("result", "failure");
            return resultMap;
        }

        if(jsonMap.get("result") == null) {
            String msg = "JSON 조회 실패3 - result가 없음";
            log.error(msg);
            resultMap.put("result", "failure");
            resultMap.put("msg", msg);
            return resultMap;
        }

        HashMap<String, Object> innerMap = (HashMap<String, Object>) jsonMap.get("result");

        if(innerMap.get("success") == null) {
            String msg = "JSON 조회 실패4 - success가 없음";
            log.error(msg);
            resultMap.put("result", "failure");
            resultMap.put("msg", msg);
            return resultMap;
        }

        if(!(boolean)innerMap.get("success")) {
            String msg = "JSON 조회 실패5 - success가 true가 아님";
            log.error(msg);
            resultMap.put("result", "failure");
            resultMap.put("msg", msg);
            return resultMap;
        }

        if(innerMap.get("totalPage") != null && page == 1) {
            resultMap.put("totalPage", innerMap.get("totalPage"));
        }

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

        resultMap.put("list", resultList);

        return resultMap;
    }

}
