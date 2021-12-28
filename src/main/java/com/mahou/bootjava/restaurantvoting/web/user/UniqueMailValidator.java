package com.mahou.bootjava.restaurantvoting.web.user;

import com.mahou.bootjava.restaurantvoting.HasIdAndEmail;
import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.repository.UserRepository;
import com.mahou.bootjava.restaurantvoting.util.SecurityUtil;
import com.mahou.bootjava.restaurantvoting.web.ExceptionInfoHandler;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {
    private final UserRepository repository;
    private final HttpServletRequest request;

    public UniqueMailValidator(UserRepository repository, @Nullable HttpServletRequest request) {
        this.repository = repository;
        this.request = request;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            User dbUser = repository.findByEmailIgnoreCase(user.getEmail().toLowerCase()).orElseThrow(() ->
                    new NotFoundException("User not found, id: " + user.id()));
            if (dbUser != null) {
                Assert.notNull(request, "HttpServletRequest missed");
                if (request.getMethod().equals("PUT") || (request.getMethod().equals("POST") && user.getId() != null)) {  // update for REST(PUT) and UI(POST)
                    int dbId = dbUser.id();
                    // it is ok, if update ourself
                    if (user.getId() != null && dbId == user.id()) return;

                    // workaround for update with user.id=null in request body
                    // ValidationUtil.assureIdConsistent (id setter) called after this validation
                    String requestURI = request.getRequestURI();
                    if (requestURI.endsWith("/" + dbId) || (dbId == SecurityUtil.get().getId() && requestURI.contains("/profile"))) return;
                }
                errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
            }
        }
    }
}
