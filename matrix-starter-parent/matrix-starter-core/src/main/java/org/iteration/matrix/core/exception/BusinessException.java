package org.iteration.matrix.core.exception;

import org.iteration.matrix.core.message.MessageAccessor;

public class BusinessException extends Exception {
    public static final String ERROR_CODE = "E_MS_BIZ_SO00";

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String errorCode = ERROR_CODE;

    public BusinessException(String errorCode, Object[] messageParameters) {
        super(MessageAccessor.getMessage(errorCode, messageParameters).getDesc());
        this.setErrorCode(errorCode);
    }

    public BusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.setErrorCode(errorCode);
    }

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
