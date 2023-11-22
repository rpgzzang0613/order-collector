package com.amiiboroom.ordercollector.dto.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private int idx;

    private String id;
    private String pw;
    private String email;
    private String name;
    private String status;
    private String role;
    private LocalDateTime last_login_date;
    private String last_login_ip;
    private LocalDateTime regi_date;

}
