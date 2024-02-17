package com.amiiboroom.ordercollector.util.webscraping.collector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SofranoMallOrderCollector implements OrderCollector {

    private final WebDriver driver;

    @Override
    public boolean login(HashMap<String, Object> requestMap) throws InterruptedException {
        boolean isLoggedIn = false;

        driver.get("https://sofrano.com/member/login.html");
        Thread.sleep(1500);

        WebElement idInput = driver.findElement(By.cssSelector("#member_id"));
        WebElement pwInput = driver.findElement(By.cssSelector("#member_passwd"));

        idInput.sendKeys(requestMap.get("id").toString());
        pwInput.sendKeys(requestMap.get("pw").toString());
        pwInput.sendKeys(Keys.ENTER);
        Thread.sleep(1500);

        try {
            WebElement logoutBtn = driver.findElement(By.cssSelector("#JS_topMenu .xans-layout-statelogon a"));
            if(logoutBtn != null && logoutBtn.getText().equals("로그아웃"))  {
                isLoggedIn = true;
            }
        }catch(NoSuchElementException e) {
            // 못찾으면 그냥 isLoggedIn 값 안바꿔서 실패처리
        }

        return isLoggedIn;
    }

    @Override
    public HashMap<String, Object> getOrderList() throws InterruptedException {
        HashMap<String, Object> resultMap = new HashMap<>();

        List<HashMap<String, Object>> resultList = new ArrayList<>();

        String baseUrl = "https://sofrano.com/myshop/order/list.html";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("order_status", "all")
                .queryParam("history_start_date", "1999-01-01")
                .queryParam("history_end_date", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        String fullUrl = builder.toUriString();

        driver.get(fullUrl);
        Thread.sleep(2000);

        int totalOrderCnt = Integer.parseInt(driver.findElement(By.cssSelector("#xans_myshop_total_orders")).getText());
        List<WebElement> firstPageOrderList = driver.findElements(By.cssSelector(".xans-myshop-orderhistorylistitem tbody tr"));
        int firstPageOrderCnt = firstPageOrderList.size();  // 내 주문리스트 첫페이지의 주문 개수

        // 한페이지에 몇개 보여주는지 확인해보고 페이지수 계산해보고 데이터 뽑아내는 로직 완성 필요

        return resultMap;
    }
}
