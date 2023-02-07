package org.iteration.matrix.core.restful;

import lombok.Data;
import org.iteration.matrix.core.message.MessageAccessor;
import org.iteration.matrix.core.message.Message;

@Data
public class APIResponse<T> {

    private ApiResponseStatus status;
    private String code;
    private String message;
    private T response;

    private APIResponse() {
    }

    public static <T> APIResponse<T> buildSuccess(T response) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.status = ApiResponseStatus.SUCCESS;
        apiResponse.code = "200";
        apiResponse.response = response;
        return apiResponse;
    }

    public static <T> APIResponse<T> buildException(String code, T response) {
        Message message = MessageAccessor.getMessage(code);
        APIResponse apiResponse = new APIResponse();
        apiResponse.status = ApiResponseStatus.ERROR;
        apiResponse.code = code;
        apiResponse.message = message.getDesc();
        apiResponse.response = response;
        return apiResponse;
    }
}
