package com.amiiboroom.ordercollector.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.RandomSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 양방향 암호화용 Jasypt 라이브러리 설정
 */
@Configuration
public class JasyptConfig {

    // secret yml 파일에서 가져옴
    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean
    public StandardPBEStringEncryptor jasyptStringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setSaltGenerator(new RandomSaltGenerator());

        return encryptor;
    }

}
