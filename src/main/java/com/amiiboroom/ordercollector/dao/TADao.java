package com.amiiboroom.ordercollector.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TADao {

    int TAI01(HashMap<String, Object> requestMap);
    HashMap<String, Object> TAS01(HashMap<String, Object> requestMap);
    HashMap<String, Object> TAS02(HashMap<String, Object> requestMap);

}
