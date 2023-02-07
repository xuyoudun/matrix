package org.iteration.matrix.core.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class APIResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIResponseAdvice.class);

    @Autowired
    private ObjectMapper objectMapper;

    /*仅当方法或类没有标记@NoAPIResponse才自动包装*/
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getParameterType() != APIResponse.class
                && AnnotationUtils.findAnnotation(returnType.getMethod(), NoAPIResponse.class) == null
                && AnnotationUtils.findAnnotation(returnType.getDeclaringClass(), NoAPIResponse.class) == null;
    }

    /*自动包装外层APIResposne响应*/
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        APIResponse apiResponse = APIResponse.buildSuccess(body);
        if (body instanceof String) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return objectMapper.writeValueAsString(apiResponse);
        } else {
            return apiResponse;
        }
    }
}
