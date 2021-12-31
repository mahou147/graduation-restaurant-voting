package com.mahou.bootjava.restaurantvoting.web.user;

import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.repository.UserRepository;
import com.mahou.bootjava.restaurantvoting.to.UserTo;
import com.mahou.bootjava.restaurantvoting.util.JsonUtil;
import com.mahou.bootjava.restaurantvoting.util.UserUtil;
import com.mahou.bootjava.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.mahou.bootjava.restaurantvoting.UserTestData.*;
import static com.mahou.bootjava.restaurantvoting.web.user.ProfileController.URL;
import static com.mahou.bootjava.restaurantvoting.web.user.UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findById(USER_ID).isPresent());
        assertTrue(userRepository.findById(ADMIN_ID).isPresent());
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newemail@ya.ru", "New", "Surname", "newPass");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, USER_MAIL, "newName", "newSurname", "newPassword");
        perform(MockMvcRequestBuilders.put(URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getById(USER_ID), UserUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo newTo = new UserTo(null, null, null, null, null);
        perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, "password", null, null, null);
        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, ADMIN_MAIL, "firstName", "newSurname", "newPassword");
        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_EMAIL)));
    }
}