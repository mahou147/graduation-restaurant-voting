package com.mahou.bootjava.restaurantvoting.web;

import com.mahou.bootjava.restaurantvoting.error.ErrorInfo;
import com.mahou.bootjava.restaurantvoting.error.ErrorType;
import com.mahou.bootjava.restaurantvoting.error.IllegalRequestDataException;
import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.mahou.bootjava.restaurantvoting.error.ErrorType.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionInfoHandler {

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_ADDRESS = "exception.restaurant.duplicateAddress";
    public static final String EXCEPTION_DUPLICATE_TITLE = "exception.dish.duplicateTitle";
    public static final String EXCEPTION_DUPLICATE_VOTE = "exception.vote.duplicateDaily";
    public static final String EXCEPTION_TIME_OUT = "exception.vote.timeOut";

    private static final Map<String, String> CONSTRAINS_I18N_MAP = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "restaurant_unique_address_idx", EXCEPTION_DUPLICATE_ADDRESS,
            "dish_unique_dish_title_idx", EXCEPTION_DUPLICATE_TITLE,
            "user_unique_vote_daily_idx", EXCEPTION_DUPLICATE_VOTE,
            "check_hour", EXCEPTION_TIME_OUT);

    private final MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINS_I18N_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, messageSourceAccessor.getMessage(entry.getValue()));
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(messageSourceAccessor::getMessage)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, details);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    public ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logStackTrace, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logStackTrace, errorType);
        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(req.getRequestURL(), errorType,
                        messageSourceAccessor.getMessage(errorType.getErrorCode()),
                        details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)})
                );
    }
}
