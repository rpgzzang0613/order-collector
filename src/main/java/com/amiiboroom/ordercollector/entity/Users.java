package com.amiiboroom.ordercollector.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    private String id;
    private String pw;
    private String email;
    private String name;
    private String status;
    private String role;

    private LocalDateTime last_login_date;
    private String last_login_ip;

    @CreationTimestamp
    private LocalDateTime regi_date;

    @Builder
    public Users(String id, String pw, String email, String name, String status, String role, LocalDateTime last_login_date, String last_login_ip) {
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.name = name;
        this.status = status;
        this.role = role;
        this.last_login_date = last_login_date;
        this.last_login_ip = last_login_ip;
    }

}
