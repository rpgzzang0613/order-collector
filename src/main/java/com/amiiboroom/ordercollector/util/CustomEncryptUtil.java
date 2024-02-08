package com.amiiboroom.ordercollector.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomEncryptUtil {

    private final StandardPBEStringEncryptor dataEncryptor;     // 양방향
    private final PasswordEncoder passwordEncoder;              // 단방향

    /**
     * 비밀번호를 제외한 다른 데이터 양방향 암호화
     * @param plainStr - 평문 데이터
     * @return 암호화된 데이터
     */
    public String encrypt(String plainStr) {
        String encryptedStr = "";

        if(plainStr != null && !plainStr.trim().isEmpty()) {
            encryptedStr = dataEncryptor.encrypt(plainStr.trim());
        }

        return encryptedStr.trim();
    }

    /**
     * 비밀번호를 제외한 다른 데이터 복호화
     * @param encryptedStr - 암호화된 데이터
     * @return 복호화된 데이터
     */
    public String decrypt(String encryptedStr) {
        String plainStr = "";

        if(encryptedStr != null && !encryptedStr.trim().isEmpty()) {
            plainStr = dataEncryptor.decrypt(encryptedStr.trim());
        }

        return plainStr.trim();
    }

    /**
     * 비밀번호 단방향 암호화
     * @param plainPw - 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    public String encodePassword(String plainPw) {
        String encodedPw = "";
        if (plainPw != null && !plainPw.trim().isEmpty()) {
            encodedPw = passwordEncoder.encode(plainPw.trim());
        }

        return encodedPw.trim();
    }

    public HashMap<String, Object> encryptMapValues(HashMap<String, Object> plainMap, String[] keysToEncrypt) {
        HashMap<String, Object> encryptedMap = new HashMap<>(plainMap);

        encryptedMap.forEach((k, v) -> {
            boolean isEncodeValue = Arrays.stream(keysToEncrypt).anyMatch(k2 -> k2.equalsIgnoreCase(k));

            if("user_pw".equalsIgnoreCase(k) && v != null && !v.toString().trim().isEmpty()) {
                encryptedMap.put(k, passwordEncoder.encode(v.toString().trim()));
            }else if(isEncodeValue && v != null && !v.toString().trim().isEmpty()) {
                encryptedMap.put(k, dataEncryptor.encrypt(v.toString().trim()));
            }
        });

        return encryptedMap;
    }

    public HashMap<String, Object> decryptMapValues(HashMap<String, Object> encryptedMap, String[] keysToDecrypt) {
        HashMap<String, Object> decryptedMap = new HashMap<>(encryptedMap);

        decryptedMap.forEach((k, v) -> {
            boolean isDecryptValue = Arrays.stream(keysToDecrypt).anyMatch(k2 -> k2.equalsIgnoreCase(k));

            if(isDecryptValue && v != null && !v.toString().trim().isEmpty()) {
                decryptedMap.put(k, dataEncryptor.decrypt(v.toString().trim()));
            }
        });

        return decryptedMap;
    }

}
