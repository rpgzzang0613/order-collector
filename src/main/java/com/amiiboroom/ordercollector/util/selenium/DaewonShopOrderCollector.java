package com.amiiboroom.ordercollector.util.selenium;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@RequiredArgsConstructor
@Slf4j
public class DaewonShopOrderCollector implements OrderCollector {

    private final SeleniumUtil seleniumUtil;

    @Override
    public boolean login(HashMap<String, Object> requestMap) {
        return false;
    }

    @Override
    public HashMap<String, Object> getOrderList() {
        return null;
    }
}
