package com.jh.common.enums;

public class ApiResponseContents {
    /** 200 메시지 접두문자 */
    public static final String MESSAGE_200_PREFIX = "<pre>Request han been successfully<br/>{<br/>  \"data\": {}, <font color=\"red\">data 항목은 ";
    /** 200 메시지 접미문자 */
    public static final String MESSAGE_200_POSTFIX = " 참고</font><br/>  \"statusCode\": 200,<br/>  \"resultMessage\": \"정상처리 되었습니다.\",<br/>  \"detailMessage\": \"처리중 오류 발생시 상세 메세지 노출.\"<br/>}</pre>";
    /** 404 메시지 */
    public static final String MESSAGE_404 = "Page Not Found";
    /** 500 메시지 */
    public static final String MESSAGE_500 = "An error occurred";

    /** common 메시지 */
    public static final String MESSAGE_TEST = "테스트 성공.";

    /** 완료 */
    public static final String COMPLETE = "정상처리 되었습니다.";
    public static final String SEARCH_COMPLETE = "검색 조회 성공.";
    /** 오류 */
    public static final String ERROR = "처리 중 오류가 발생하였습니다.";
    public static final String SEARCH_ERROR = "검색 조회 실패.";
}
