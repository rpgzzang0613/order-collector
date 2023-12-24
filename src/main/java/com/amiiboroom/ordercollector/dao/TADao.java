package com.amiiboroom.ordercollector.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TADao {

    /** INSERT **/
    // 유저 회원가입
    int TAI01(HashMap<String, Object> requestMap);

    /** UPDATE **/
    // 임시 비밀번호 설정
    int TAU01(HashMap<String, Object> requestMap);

    // 비밀번호 재설정
    int TAU02(HashMap<String, Object> requestMap);

    /** DELETE **/

    /** SELECT **/
    // 아이디 중복 체크 (아이디로 가입유무 조회)
    HashMap<String, Object> TAS01(HashMap<String, Object> requestMap);

    // 로그인 (아이디, 비번으로 유저정보 조회)
    HashMap<String, Object> TAS02(HashMap<String, Object> requestMap);

    // 아이디 찾기 (이메일, 이름으로 아이디 조회)
    HashMap<String, Object> TAS03(HashMap<String, Object> requestMap);

}
