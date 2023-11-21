package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.dto.menu.DetailMenuDTO;
import com.amiiboroom.ordercollector.dto.menu.MenuDTO;
import com.amiiboroom.ordercollector.util.enums.ApiMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public ResponseEntity<ApiResult> getUserMenuByRole(String role) {
        ApiResult apiResult;
        HashMap<String, MenuDTO> menuMap;

        menuMap = createMenu(role);
        apiResult = new ApiResult(ApiMessage.SUCCESS, menuMap);

        return ResponseEntity.ok(apiResult);
    }

    // 추후 하드코딩 걷어내기
    private HashMap<String, MenuDTO> createMenu(String role) {
        HashMap<String, MenuDTO> resultMap = new HashMap<>();

        List<DetailMenuDTO> viewList = new ArrayList<>();
        viewList.add(new DetailMenuDTO("구매 내역", "/orders"));
        MenuDTO viewMenu = new MenuDTO("조회 메뉴", viewList);
        resultMap.put("view", viewMenu);

        MenuDTO updateMenu = new MenuDTO("구매 내역 갱신", null);
        resultMap.put("update", updateMenu);

        if("ROLE_ADMIN".equals(role)) {
            List<DetailMenuDTO> adminList = new ArrayList<>();
            adminList.add(new DetailMenuDTO("유저 관리", "/admin/users"));
            MenuDTO adminMenu = new MenuDTO("관리자 메뉴", adminList);
            resultMap.put("admin", adminMenu);
        }

        return resultMap;
    }

}
