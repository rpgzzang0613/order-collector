package com.amiiboroom.ordercollector.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface TBDao {

    List<HashMap<String, Object>> TBS01(HashMap<String, Object> requestMap);

}
