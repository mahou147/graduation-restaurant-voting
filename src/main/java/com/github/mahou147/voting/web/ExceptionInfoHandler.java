package com.github.mahou147.voting.web;

import com.github.mahou147.voting.error.*;
import com.github.mahou147.voting.util.ValidationUtil;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionInfoHandler {

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_ADDRESS = "exception.restaurant.duplicateAddress";
    public static final String EXCEPTION_DUPLICATE_TITLE = "exception.menuItem.duplicateTitle";
    public static final String EXCEPTION_DUPLICATE_VOTE = "exception.vote.duplicateDaily";
    public static final String EXCEPTION_DUPLICATE_MENU_DATE = "exception.menu.duplicateDate";
    public static final String EXCEPTION_TIME_OUT = "The voting is over for today. Try tomorrow ";

    private static final Map<String, String> CONSTRAINS_I18N_MAP = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "restaurant_unique_address_idx", EXCEPTION_DUPLICATE_ADDRESS,
            "menu_item_unique_title_idx", EXCEPTION_DUPLICATE_TITLE,
            "user_unique_vote_daily_idx", EXCEPTION_DUPLICATE_VOTE,
            "unique_menu_of_the_day", EXCEPTION_DUPLICATE_MENU_DATE);

    private final MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, ErrorType.DATA_NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINS_I18N_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, false, ErrorType.VALIDATION_ERROR,
                            messageSourceAccessor.getMessage(entry.getValue()));
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, ErrorType.DATA_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(messageSourceAccessor::getMessage)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, false, ErrorType.VALIDATION_ERROR, details);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, ErrorType.VALIDATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorInfo> appException(HttpServletRequest req, AppException e) {
        return logAndGetErrorInfo(req, e, false, e.getType(), e.getMsgCode());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorInfo> wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetErrorInfo(req, e, false, ErrorType.WRONG_REQUEST);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    public ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logStackTrace,
                                                        ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logStackTrace, errorType);
        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(req.getRequestURL(), errorType,
                        details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)})
                );
    }
}
