package com.amiiboroom.ordercollector.util.enums;

/**
 * HTTP 통신 자체의 에러가 아닌
 * Backend 단에서 코드가 실행되는 도중에 발생한 에러이거나
 * DB와 응답시 발생한 에러를 뱉어주기 위한 메시지
 */
public enum BackendMessage {

    DATA_SAVE_SUCCESS,
    SUCCESS,
    FAILED,
    FAILED_TO_SHOP_LOGIN,
    FAILED_TO_SCRAPING,
    DATA_NOT_FOUND,
    DATA_ALREADY_EXISTS,
    INVALID_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    INTERNAL_SERVER_ERROR

}
