package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dao.TADao;
import com.amiiboroom.ordercollector.dao.TBDao;
import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.util.enums.ApiMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final TADao TADao;
    private final TBDao TBDao;
    private final StandardPBEStringEncryptor dataEncryptor;     // 양방향
    private final PasswordEncoder passwordEncoder;              // 단방향

    public ResponseEntity<ApiResult> signup(HashMap<String, Object> requestMap) {
        ApiResult apiResult;

//        int res = TADao.TAI01(encodeUserInfo(requestMap));
        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);
        int res = TADao.TAI01(changedMap);

        if(res > 0) {
            apiResult = new ApiResult(ApiMessage.DATA_SAVE_SUCCESS, null);
        }else {
            apiResult = new ApiResult(ApiMessage.FAILED, null);
        }

        return ResponseEntity.ok(apiResult);
    }

    public ResponseEntity<ApiResult> login(HashMap<String, Object> requestMap) {
        ApiResult apiResult;

        HashMap<String, Object> resultMap = TADao.TAS02(requestMap);

        if(resultMap != null && !resultMap.isEmpty()) {
//            apiResult = new ApiResult(ApiMessage.SUCCESS, decodeUserInfo(resultMap));
            apiResult = new ApiResult(ApiMessage.SUCCESS, resultMap);
        }else {
            apiResult = new ApiResult(ApiMessage.FAILED, null);
        }

        return ResponseEntity.ok(apiResult);
    }

    public ResponseEntity<ApiResult> getUserMenuByRole(HashMap<String, Object> requestMap) {
        ApiResult apiResult;

        HashMap<String, Object> menuMap = TBDao.TBS01(requestMap);

        if(menuMap != null && !menuMap.isEmpty()) {
            apiResult = new ApiResult(ApiMessage.SUCCESS, menuMap);
        }else {
            apiResult = new ApiResult(ApiMessage.FAILED, menuMap);
        }

        return ResponseEntity.ok(apiResult);
    }

    /** 아래부턴 클래스 내부 사용 메소드 **/
    private HashMap<String, Object> changeKeyToUppercase(HashMap<String, Object> map) {
        HashMap<String, Object> changedMap = new HashMap<>();
        map.forEach((k, v) -> changedMap.put(k.toUpperCase(), v));

        return changedMap;
    }

    private HashMap<String, Object> encodeUserInfo(HashMap<String, Object> map) {
        HashMap<String, Object> encodedMap = new HashMap<>(map);
        encodedMap.forEach((k, v) -> {
            if("user_id".equals(k) || "user_name".equals(k) || "email".equals(k)) {
                encodedMap.put(k, dataEncryptor.encrypt(v.toString()));
            }else if("user_pw".equals(k)) {
                encodedMap.put(k, passwordEncoder.encode(v.toString()));
            }
        });

        return encodedMap;
    }

    private HashMap<String, Object> decodeUserInfo(HashMap<String, Object> map) {
        HashMap<String, Object> decodedMap = new HashMap<>(map);
        decodedMap.forEach((k, v) -> {
            if("user_id".equals(k) || "user_name".equals(k) || "email".equals(k)) {
                decodedMap.put(k, dataEncryptor.decrypt(v.toString()));
            }
        });

        return decodedMap;
    }

}
