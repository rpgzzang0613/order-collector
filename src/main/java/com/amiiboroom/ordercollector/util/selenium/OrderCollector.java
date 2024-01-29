package com.amiiboroom.ordercollector.util.selenium;

import java.util.HashMap;
import java.util.List;

public interface OrderCollector {

    boolean login(HashMap<String, Object> requestMap);

    List<HashMap<String, Object>> getOrderList();

}
