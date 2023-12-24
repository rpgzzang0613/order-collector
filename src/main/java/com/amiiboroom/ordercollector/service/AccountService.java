package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dao.TADao;
import com.amiiboroom.ordercollector.dao.TBDao;
import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.util.enums.BackendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final TADao taDao;
    private final TBDao tbDao;
    private final StandardPBEStringEncryptor dataEncryptor;     // 양방향
    private final PasswordEncoder passwordEncoder;              // 단방향

    public ResponseEntity<BackendResult> checkAccountExists(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);
//        HashMap<String, Object> checkMap = taDao.TAS01(encodeUserInfo(changedMap));
        HashMap<String, Object> checkMap = taDao.TAS01(changedMap);
        if(checkMap != null && !checkMap.isEmpty()) {
            backendResult = new BackendResult(BackendMessage.SUCCESS, checkMap);
        }else {
            backendResult = new BackendResult(BackendMessage.FAILED, checkMap);
        }

        return ResponseEntity.ok(backendResult);
    }

    public ResponseEntity<BackendResult> signup(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);
//        int res = taDao.TAI01(encodeUserInfo(changedMap));
        int res = taDao.TAI01(changedMap);

        if(res > 0) {
            backendResult = new BackendResult(BackendMessage.DATA_SAVE_SUCCESS, null);
        }else {
            backendResult = new BackendResult(BackendMessage.FAILED, null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(backendResult);
    }

    public ResponseEntity<BackendResult> login(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);
        HashMap<String, Object> userMap = taDao.TAS02(changedMap);

        if(userMap != null && !userMap.isEmpty()) {
//            backendResult = new ApiResult(BackendMessage.SUCCESS, decodeUserInfo(userMap));
            backendResult = new BackendResult(BackendMessage.SUCCESS, userMap);
        }else {
            backendResult = new BackendResult(BackendMessage.FAILED, null);
        }

        return ResponseEntity.ok(backendResult);
    }

    public ResponseEntity<BackendResult> findUserIdByNameAndEmail(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);
//        HashMap<String, Object> idMap = taDao.TAS03(encodeUserInfo(changedMap));
        HashMap<String, Object> idMap = taDao.TAS03(changedMap);
        if(idMap != null && !idMap.isEmpty()) {
            backendResult = new BackendResult(BackendMessage.SUCCESS, idMap);
        }else {
            backendResult = new BackendResult(BackendMessage.DATA_NOT_FOUND, null);
        }

        return ResponseEntity.ok(backendResult);
    }

    public ResponseEntity<BackendResult> updatePwdTemp(HashMap<String, Object> requestMap) {
//        taDao.TAU01(requestMap);
        return null;
    }

    public ResponseEntity<BackendResult> getMenusByRole(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);
//        List<HashMap<String, Object>> menuList = tbDao.TBS01(encodeUserInfo(changedMap));
        List<HashMap<String, Object>> menuList = tbDao.TBS01(changedMap);

        if(menuList != null && !menuList.isEmpty()) {
            backendResult = new BackendResult(BackendMessage.SUCCESS, menuList);
        }else {
            backendResult = new BackendResult(BackendMessage.FAILED, menuList);
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
            if("user_id".equalsIgnoreCase(k) || "user_name".equalsIgnoreCase(k) || "email".equalsIgnoreCase(k)) {
                encodedMap.put(k, dataEncryptor.encrypt(v.toString()));
            }else if("user_pw".equalsIgnoreCase(k)) {
                encodedMap.put(k, passwordEncoder.encode(v.toString()));
            }
        });

        return encodedMap;
    }

    private HashMap<String, Object> decodeUserInfo(HashMap<String, Object> map) {
        HashMap<String, Object> decodedMap = new HashMap<>(map);
        decodedMap.forEach((k, v) -> {
            if("user_id".equalsIgnoreCase(k) || "user_name".equalsIgnoreCase(k) || "email".equalsIgnoreCase(k)) {
                decodedMap.put(k, dataEncryptor.decrypt(v.toString()));
            }
        });

        return decodedMap;
    }

}
