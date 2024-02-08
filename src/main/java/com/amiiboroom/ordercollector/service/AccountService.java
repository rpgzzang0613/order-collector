package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dao.TADao;
import com.amiiboroom.ordercollector.dao.TBDao;
import com.amiiboroom.ordercollector.dto.BackendResult;
import com.amiiboroom.ordercollector.util.CustomEncryptUtil;
import com.amiiboroom.ordercollector.util.enums.BackendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final TADao taDao;
    private final TBDao tbDao;
    private final CustomEncryptUtil encryptUtil;

    @Value("${spring.profiles.active}")
    private String activatedProfile;

    public ResponseEntity<BackendResult> checkAccountExists(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);

        if("real".equals(activatedProfile)) {
            String[] keysToEncrypt = {"user_id", "user_name", "email"};
            changedMap = encryptUtil.encryptMapValues(changedMap, keysToEncrypt);
        }

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

        if("real".equals(activatedProfile)) {
            String[] keysToEncrypt = {"user_id", "user_name", "email"};
            changedMap = encryptUtil.encryptMapValues(changedMap, keysToEncrypt);
        }

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
        if("real".equals(activatedProfile)) {
            String[] keysToEncrypt = {"user_id", "user_pw"};
            changedMap = encryptUtil.encryptMapValues(changedMap, keysToEncrypt);
        }

        HashMap<String, Object> userMap = taDao.TAS02(changedMap);

        if(userMap != null && !userMap.isEmpty()) {
            if("real".equals(activatedProfile)) {
                String[] keysToDecrypt = {"user_id", "user_name", "email"};
                userMap = encryptUtil.decryptMapValues(userMap, keysToDecrypt);
            }

            backendResult = new BackendResult(BackendMessage.SUCCESS, userMap);
        }else {
            backendResult = new BackendResult(BackendMessage.DATA_NOT_FOUND, null);
        }

        return ResponseEntity.ok(backendResult);
    }

    public ResponseEntity<BackendResult> findUserIdByNameAndEmail(HashMap<String, Object> requestMap) {
        BackendResult backendResult;

        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);

        if("real".equals(activatedProfile)) {
            String[] keysToEncrypt = {"user_id", "user_name", "email"};
            changedMap = encryptUtil.encryptMapValues(changedMap, keysToEncrypt);
        }

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

    public List<HashMap<String, Object>> getMenusByRole(HashMap<String, Object> requestMap) {
        HashMap<String, Object> changedMap = changeKeyToUppercase(requestMap);

        if("real".equals(activatedProfile)) {
            String[] keysToEncrypt = {"user_id", "user_name", "email"};
            changedMap = encryptUtil.encryptMapValues(changedMap, keysToEncrypt);
        }

        List<HashMap<String, Object>> menuList = tbDao.TBS01(changedMap);

        List<HashMap<String, Object>> bundledMenuList;
        if(menuList == null) {
            bundledMenuList = new ArrayList<>();
        }else {
            bundledMenuList = bundleMenuList(menuList);
        }

        return bundledMenuList;
    }

    /** 아래부턴 클래스 내부 사용 메소드 **/
    private HashMap<String, Object> changeKeyToUppercase(HashMap<String, Object> map) {
        HashMap<String, Object> changedMap = new HashMap<>();
        map.forEach((k, v) -> changedMap.put(k.toUpperCase(), v));

        return changedMap;
    }

    private List<HashMap<String, Object>> bundleMenuList(List<HashMap<String, Object>> menuList) {
        List<HashMap<String, Object>> bundledMenuList = new ArrayList<>();
        HashMap<String, Object> currentMenu = null;
        List<HashMap<String, Object>> subMenuList = null;

        for(HashMap<String, Object> menu : menuList) {
            int menuDepth = (int) menu.get("menu_depth");

            if(menuDepth == 1) {
                if(currentMenu != null) {
                    bundledMenuList.add(currentMenu);
                }

                currentMenu = new HashMap<>();
                currentMenu.put("rno", menu.get("rno"));
                currentMenu.put("code", menu.get("code"));
                currentMenu.put("code_desc", menu.get("code_desc"));

                subMenuList = new ArrayList<>();
                currentMenu.put("sub_menus", subMenuList);
            }else if(menuDepth == 2 && subMenuList != null) {
                HashMap<String, Object> subMenu = new HashMap<>();
                subMenu.put("rno", menu.get("rno"));
                subMenu.put("code", menu.get("code"));
                subMenu.put("code_desc", menu.get("code_desc"));
                subMenuList.add(subMenu);
            }
        }

        if(currentMenu != null) {
            bundledMenuList.add(currentMenu);
        }

        return bundledMenuList;
    }

}
