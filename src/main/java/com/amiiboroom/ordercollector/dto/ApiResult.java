package com.amiiboroom.ordercollector.dto;

import com.amiiboroom.ordercollector.util.enums.ApiMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * API 최종 리턴값을 담기 위한 객체를 만드는 클래스
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ApiResult {
     private ApiMessage message;
     private Object data;

     public ApiResult(ApiMessage message, Object data) {
          this.message = message;
          this.data = data;
     }
}
