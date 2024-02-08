package com.amiiboroom.ordercollector.util.webscraping.collector;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;

public interface OrderCollector {

    boolean login(HashMap<String, Object> requestMap) throws InterruptedException;

    HashMap<String, Object> getOrderList() throws JsonProcessingException, InterruptedException;

}
