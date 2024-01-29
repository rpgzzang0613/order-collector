package com.amiiboroom.ordercollector.util.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;

@Component
public class SeleniumUtil {

    public WebDriver initWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");          // 최대 창크기로 실행 (안보일때도 적용됨)
        options.addArguments("--headless=new");             // 안보이고 백그라운드에서 동작하게 설정
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-logging"));  // 불필요 로그 제거

        WebDriverManager.chromedriver().setup();            // 크롬드라이버 버전 체크 및 매칭
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L)); // 페이지 대기시간 10초 (넘어가도록 응답없으면 예외발생)

        return driver;
    }

    public void quitWebDriver(WebDriver driver) {
        driver.quit();
    }

}
