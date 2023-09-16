package com.amiiboroom.ordercollector.dto.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestExampleDTO {

    private int idx;
    private String name;
    private LocalDateTime regiDate;

}
