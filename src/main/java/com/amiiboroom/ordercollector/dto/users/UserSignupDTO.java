package com.amiiboroom.ordercollector.dto.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSignupDTO {

    private String id;
    private String pw;
    private String email;
    private String name;
    private final String status = "WAIT";
    private final String role = "USER";

}
