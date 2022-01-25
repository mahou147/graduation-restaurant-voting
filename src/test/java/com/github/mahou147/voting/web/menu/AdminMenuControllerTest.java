package com.github.mahou147.voting.web.menu;

import com.github.mahou147.voting.MenuTestData;
import com.github.mahou147.voting.UserTestData;
import com.github.mahou147.voting.error.NotFoundException;
import com.github.mahou147.voting.model.Menu;
import com.github.mahou147.voting.service.MenuService;
import com.github.mahou147.voting.util.JsonUtil;
import com.github.mahou147.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.mahou147.voting.MenuTestData.*;
import static com.github.mahou147.voting.RestaurantTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {
    private static final String URL = AdminMenuController.URL + '/';

    @Autowired
    private MenuService service;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + EATALY_CURRENT_MENU_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(eataly_current_menu));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + MenuTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + EATALY_CURRENT_MENU_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "by-restaurant/" + EATALY_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(eataly_current_menu, eataly_menu));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + EATALY_CURRENT_MENU_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.getWithDishes(EATALY_CURRENT_MENU_ID));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Menu newMenu = new Menu(null, TOMORROW);
        newMenu.setRestaurant(eataly);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .param("restId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)));

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(service.getWithDishes(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Menu updated = eataly_current_menu;
        updated.setActualDate(TOMORROW);
        updated.setRestaurant(ramen);
        perform(MockMvcRequestBuilders.put(URL + EATALY_CURRENT_MENU_ID).contentType(MediaType.APPLICATION_JSON)
                .param("restId", "2")
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(service.getWithDishes(EATALY_CURRENT_MENU_ID), updated);
    }
}