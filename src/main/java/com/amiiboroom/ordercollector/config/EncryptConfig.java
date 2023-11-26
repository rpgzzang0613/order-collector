package com.amiiboroom.ordercollector.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.StringFixedSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 양방향 암호화용 Jasypt 라이브러리 설정
 */
@Configuration
public class EncryptConfig {

    // secret yml 파일에서 가져옴
    @Value("${jasypt.encryptor.password}")
    private String password;

    @Value("${jasypt.encryptor.salt}")
    private String salt;

    @Bean
    public StandardPBEStringEncryptor jasyptStringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setSaltGenerator(new StringFixedSaltGenerator(salt));

        return encryptor;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
