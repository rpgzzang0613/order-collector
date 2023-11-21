package com.amiiboroom.ordercollector.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class DetailMenuDTO {

    private String name;
    private String url;

}
