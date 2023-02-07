package org.iteration.matrix.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.iteration.matrix.core.restful.APIResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler({Throwable.class})
    public APIResponse<?> handleThrowable(Throwable t) {
        log.info("exception process ", t);
        return APIResponse.buildException("error.error", t.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public APIResponse<?> process(BusinessException exception) {
        log.info("exception process ", exception);
        return APIResponse.buildException(exception.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler(StaleObjectStateException.class)
    public APIResponse<?> process(StaleObjectStateException exception) {
        log.info("exception process ", exception);
        return APIResponse.buildException("error.row_was_updated_or_deleted", exception.getMessage());
    }

}
