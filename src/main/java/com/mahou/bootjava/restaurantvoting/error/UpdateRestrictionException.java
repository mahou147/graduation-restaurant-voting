package com.mahou.bootjava.restaurantvoting.error;

public class UpdateRestrictionException extends AppException {
    public static final String EXCEPTION_UPDATE_RESTRICTION = "exception.user.updateRestriction";

    public UpdateRestrictionException() {
        super(EXCEPTION_UPDATE_RESTRICTION, ErrorType.VALIDATION_ERROR);
    }
}
