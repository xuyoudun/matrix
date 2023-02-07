package org.iteration.matrix.core.exception;

import org.iteration.matrix.core.message.MessageAccessor;

public class ApplicationException extends RuntimeException {

    public static final String ERROR_CODE = "E_MS_SYS_SZ01";


    /**
     *
     */
    private String errorCode = ERROR_CODE;

    private static final long serialVersionUID = 1L;

    public ApplicationException(String errorCode, Object[] messageParameters) {
        super(MessageAccessor.getMessage(errorCode, messageParameters).getDesc());
        this.setErrorCode(errorCode);
    }

    public ApplicationException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.setErrorCode(errorCode);
    }

    public ApplicationException(String errorMessage) {
        super(errorMessage);
    }

    public ApplicationException(Throwable cause, Object... parameters) {
        super(cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
