package com.amiiboroom.ordercollector.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class MenuDTO {

    String name;
    List<DetailMenuDTO> detailMenus;

}
