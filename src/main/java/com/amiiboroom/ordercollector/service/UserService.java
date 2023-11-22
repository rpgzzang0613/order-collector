package com.amiiboroom.ordercollector.service;

import com.amiiboroom.ordercollector.dto.ApiResult;
import com.amiiboroom.ordercollector.dto.menu.DetailMenuDTO;
import com.amiiboroom.ordercollector.dto.menu.MenuDTO;
import com.amiiboroom.ordercollector.dto.users.UserDTO;
import com.amiiboroom.ordercollector.dto.users.UserSignupDTO;
import com.amiiboroom.ordercollector.entity.Users;
import com.amiiboroom.ordercollector.repository.UserRepository;
import com.amiiboroom.ordercollector.util.enums.ApiMessage;
import com.amiiboroom.ordercollector.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StandardPBEStringEncryptor dataEncryptor;     // 양방향
    private final PasswordEncoder passwordEncoder;              // 단방향

    public ResponseEntity<ApiResult> signup(UserSignupDTO userSignupDTO) {
        UserSignupDTO encodedDTO = encodeUserSignupDto(userSignupDTO);
        Users entity = userMapper.userSignupDtoToEntity(encodedDTO);
        Users savedEntity = userRepository.save(entity);

        ApiResult apiResult = new ApiResult(ApiMessage.DATA_SAVE_SUCCESS, null);

        return ResponseEntity.ok(apiResult);
    }

    public ResponseEntity<ApiResult> checkUserExists(String id) {
        ApiResult apiResult;

        boolean exists = userRepository.existsUsersById(dataEncryptor.encrypt(id));

        if(exists) {
            apiResult = new ApiResult(ApiMessage.SUCCESS, null);
        }else {
            apiResult = new ApiResult(ApiMessage.DATA_NOT_FOUND, null);
        }

        return ResponseEntity.ok(apiResult);
    }

    public ResponseEntity<ApiResult> getUserMenuByRole(String role) {
        HashMap<String, MenuDTO> menuMap;

        menuMap = createMenu(role);
        ApiResult apiResult = new ApiResult(ApiMessage.SUCCESS, menuMap);

        return ResponseEntity.ok(apiResult);
    }

    /** 아래부턴 클래스 내부 사용 메소드 **/

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

    private UserSignupDTO encodeUserSignupDto(UserSignupDTO dto) {
        UserSignupDTO encodedDTO = new UserSignupDTO();

        encodedDTO.setId(dataEncryptor.encrypt(dto.getId()));
        encodedDTO.setName(dataEncryptor.encrypt(dto.getName()));
        encodedDTO.setEmail(dataEncryptor.encrypt(dto.getEmail()));

        encodedDTO.setPw(passwordEncoder.encode(dto.getPw()));

        return encodedDTO;
    }

    private UserDTO decodeUserDto(UserDTO dto) {
        UserDTO decodedDTO = new UserDTO();

        decodedDTO.setId(dataEncryptor.decrypt(dto.getId()));
        decodedDTO.setName(dataEncryptor.decrypt(dto.getName()));
        decodedDTO.setEmail(dataEncryptor.decrypt(dto.getEmail()));

        return decodedDTO;
    }

}
