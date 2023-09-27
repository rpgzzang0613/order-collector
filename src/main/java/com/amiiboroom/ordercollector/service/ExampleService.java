package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.util.enums.ApiMessage;
import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.dto.example.ResponseExampleDTO;
import com.amiiboroom.ordercollector.entity.Example;
import com.amiiboroom.ordercollector.util.mapper.ExampleMapper;
import com.amiiboroom.ordercollector.repository.ExampleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExampleService {

    private final ExampleRepository exampleRepository;
    private final ExampleMapper exampleMapper;

    public ResponseEntity<ApiResult> findAllExample() {
        ApiResult apiResult;
        List<ResponseExampleDTO> responseExampleDTOList;

        try {

            // DB 조회
            List<Example> exampleList = exampleRepository.findAll();

            // Entity를 DTO로 변환
            if(!exampleList.isEmpty()) {
                responseExampleDTOList = exampleList.stream()
                        .map(exampleMapper::entityToDto)
                        .toList();

                apiResult = new ApiResult(ApiMessage.SUCCESS, responseExampleDTOList);
            }else {
                apiResult = new ApiResult(ApiMessage.DATA_NOT_FOUND, Collections.emptyList());
            }

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

    public ResponseEntity<ApiResult> findOneExample(String idx) {
        ApiResult apiResult;
        ResponseExampleDTO responseExampleDTO;

        try {

            // DB 조회
            Example example = exampleRepository.findById(Long.parseLong(idx)).orElse(null);

            // Entity를 DTO로 변환
            if(example != null) {
                responseExampleDTO = exampleMapper.entityToDto(example);

                apiResult = new ApiResult(ApiMessage.SUCCESS, responseExampleDTO);
            }else {
                apiResult = new ApiResult(ApiMessage.DATA_NOT_FOUND, null);
            }

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

    public ConcurrentHashMap<String, Object> getNaverOrderListFromWeb(String id, String pw) {
        ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();

        try {

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");          // 최대 창크기로 실행 (안보일때도 적용됨)
            options.addArguments("--headless=new");          // 안보이고 백그라운드에서 동작하게 설정
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-logging"));  // 불필요 로그 제거

            WebDriverManager.chromedriver().setup();            // 크롬드라이버 버전 체크 및 매칭
            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L)); // 페이지 대기시간 10초 (넘어가도록 응답없으면 예외발생)

            driver.get("https://new-m.pay.naver.com/pcpay?serviceGroup=SHOPPING&page=1");
            Thread.sleep(1000);

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            jsExecutor.executeScript(String.format("document.getElementById('id').value = '%s'", id));
            Thread.sleep(2203);
            jsExecutor.executeScript(String.format("document.getElementById('pw').value = '%s'", pw));
            Thread.sleep(2725);

            driver.findElement(By.cssSelector("#pw")).sendKeys(Keys.ENTER);

            // 2단계 인증을 위한 대기시간
            Thread.sleep(10000);

            int page = 1;
            int totalPage = 0;
            List<HashMap<String, Object>> resultList = new ArrayList<>();

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

            resultMap.put("result", resultList);
        }catch(Exception e) {
            // 로그 저장 후 API 결과 실패로 리턴
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(out);
            e.printStackTrace(printStream);

            String stackTraceStr = out.toString();

            log.error("----- Exception StackTrace -----\n{}", stackTraceStr);

            resultMap.put("result", "failure");

            return resultMap;
        }

        return resultMap;
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
