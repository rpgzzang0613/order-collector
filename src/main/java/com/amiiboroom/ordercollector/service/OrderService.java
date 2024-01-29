package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.util.enums.BackendMessage;
import com.amiiboroom.ordercollector.util.enums.SiteType;
import com.amiiboroom.ordercollector.util.selenium.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

        SiteType siteType = SiteType.valueOf(site.toUpperCase());

        OrderCollector orderCollector;

        switch(siteType) {
            case NAVER -> orderCollector = new NaverOrderCollector(seleniumUtil);
            case SOFRANOMALL -> orderCollector = new SofranoMallOrderCollector(seleniumUtil);
            case GAMEWOORI -> orderCollector = new GameWooriOrderCollector(seleniumUtil);
            case DAEWONSHOP -> orderCollector = new DaewonShopOrderCollector(seleniumUtil);
            case BANDAIMALL -> orderCollector = new BandaiMallOrderCollector(seleniumUtil);
            case AUCTION -> orderCollector = new AuctionOrderCollector(seleniumUtil);
            case GMARKET -> orderCollector = new GMarketOrderCollector(seleniumUtil);
            case COUPANG -> orderCollector = new CoupangOrderCollector(seleniumUtil);
            case TMON -> orderCollector = new TmonOrderCollector(seleniumUtil);
            case YES24 -> orderCollector = new Yes24OrderCollector(seleniumUtil);
            case COMICSART -> orderCollector = new ComicsArtOrderCollector(seleniumUtil);
            case GUNDAMBOOM -> orderCollector = new GundamBoomOrderCollector(seleniumUtil);
            case KYOUMASHOP -> orderCollector = new KyoumaShopOrderCollector(seleniumUtil);
            case H2MALL -> orderCollector = new H2MallOrderCollector(seleniumUtil);
            case AMAZON -> orderCollector = new AmazonOrderCollector(seleniumUtil);
            case PLAYASIA -> orderCollector = new PlayAsiaOrderCollector(seleniumUtil);
            case AMIAMI -> orderCollector = new AmiAmiOrderCollector(seleniumUtil);
            case LIMITEDRUN -> orderCollector = new LimitedRunOrderCollector(seleniumUtil);
            case MERCARI -> orderCollector = new MerCariOrderCollector(seleniumUtil);
            case ALIEXPRESS -> orderCollector = new AliExpressOrderCollector(seleniumUtil);
            default -> orderCollector = null;
        }

        if(orderCollector == null) {
            // 없는 사이트가 파라미터로 넘어오면 리턴
            backendResult = new BackendResult(BackendMessage.INVALID_REQUEST, null);
            return ResponseEntity.ok(backendResult);
        }

        // 웹사이트 로그인
        boolean isLoggedIn = orderCollector.login(requestMap);

        if(!isLoggedIn) {
            // 로그인 실패시 리턴
            backendResult = new BackendResult(BackendMessage.FAILED, null);
            return ResponseEntity.ok(backendResult);
        }

        List<HashMap<String, Object>> resultList = Optional.ofNullable(orderCollector.getOrderList()).orElseGet(ArrayList::new);
        if(resultList.isEmpty()) {
            backendResult = new BackendResult(BackendMessage.DATA_NOT_FOUND, null);
        }else {
            backendResult = new BackendResult(BackendMessage.SUCCESS, resultList);
        }

        return ResponseEntity.ok(backendResult);
    }

}
