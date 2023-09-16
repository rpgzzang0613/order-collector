package com.amiiboroom.ordercollector.util.enums;

/**
 * HTTP 통신 자체의 에러가 아닌
 * API에서 코드가 실행되는 도중에 발생한 에러이거나
 * DB와 응답시 발생한 에러를 뱉어주기 위한 메시지
 */
public enum ApiMessage {

    DATA_SAVE_SUCCESS,
    SUCCESS,
    FAILED,
    DATA_NOT_FOUND,
    DATA_ALREADY_EXISTS,
    INVALID_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    INTERNAL_SERVER_ERROR

}
