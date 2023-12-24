package com.amiiboroom.ordercollector.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TADao {

    /** INSERT **/
    int TAI01(HashMap<String, Object> requestMap);

    /** UPDATE **/
    int TAU01(HashMap<String, Object> requestMap);
    int TAU02(HashMap<String, Object> requestMap);

    /** DELETE **/

    /** SELECT **/
    HashMap<String, Object> TAS01(HashMap<String, Object> requestMap);
    HashMap<String, Object> TAS02(HashMap<String, Object> requestMap);
    HashMap<String, Object> TAS03(HashMap<String, Object> requestMap);

}
