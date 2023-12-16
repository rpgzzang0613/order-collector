package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dao.TADao;
import com.amiiboroom.ordercollector.dao.TBDao;
import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.util.enums.BackendMessage;
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

    public ResponseEntity<BackendResult> signup(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

//        int res = TADao.TAI01(encodeUserInfo(requestMap));
        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);
        int res = TADao.TAI01(changedMap);

        if(res > 0) {
            backendResult = new BackendResult(BackendMessage.DATA_SAVE_SUCCESS, null);
        }else {
            backendResult = new BackendResult(BackendMessage.FAILED, null);
        }

        return ResponseEntity.ok(backendResult);
    }

    public ResponseEntity<BackendResult> login(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);
        HashMap<String, Object> resultMap = TADao.TAS02(changedMap);

        if(resultMap != null && !resultMap.isEmpty()) {
//            backendResult = new ApiResult(BackendMessage.SUCCESS, decodeUserInfo(resultMap));
            backendResult = new BackendResult(BackendMessage.SUCCESS, resultMap);
        }else {
            backendResult = new BackendResult(BackendMessage.FAILED, null);
        }

        return ResponseEntity.ok(backendResult);
    }

    public ResponseEntity<BackendResult> getUserMenuByRole(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> menuMap = TBDao.TBS01(requestMap);

        if(menuMap != null && !menuMap.isEmpty()) {
            backendResult = new BackendResult(BackendMessage.SUCCESS, menuMap);
        }else {
            backendResult = new BackendResult(BackendMessage.FAILED, menuMap);
        }

        return ResponseEntity.ok(backendResult);
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
