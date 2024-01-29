package com.amiiboroom.ordercollector.util.selenium;

import java.util.HashMap;

public interface OrderCollector {

    boolean login(HashMap<String, Object> requestMap);

    HashMap<String, Object> getOrderList();

}
