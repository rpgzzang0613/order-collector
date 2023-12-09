package com.amiiboroom.ordercollector.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TBDao {

    HashMap<String, Object> TBS01(HashMap<String, Object> requestMap);

}
