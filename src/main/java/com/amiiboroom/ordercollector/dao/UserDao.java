package com.amiiboroom.ordercollector.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface UserDao {

    int I01(HashMap<String, Object> requestMap);
    HashMap<String, Object> S01(HashMap<String, Object> requestMap);
    HashMap<String, Object> S02(String permission);

}
