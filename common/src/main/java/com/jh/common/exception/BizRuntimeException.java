package com.jh.common.exception;

public class BizRuntimeException extends BaseBizException {

    private Object additionalInfo;

    public BizRuntimeException(String errorMessage) {
        super(errorMessage);
    }

    public BizRuntimeException(Throwable cause) {
        super(cause);
    }

    public BizRuntimeException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public BizRuntimeException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public BizRuntimeException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }
}
