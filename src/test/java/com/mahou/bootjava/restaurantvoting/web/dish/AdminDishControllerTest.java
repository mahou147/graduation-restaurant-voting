package com.mahou.bootjava.restaurantvoting.web.dish;

import com.mahou.bootjava.restaurantvoting.model.Dish;
import com.mahou.bootjava.restaurantvoting.repository.DishRepository;
import com.mahou.bootjava.restaurantvoting.util.JsonUtil;
import com.mahou.bootjava.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static com.mahou.bootjava.restaurantvoting.DishTestData.*;
import static com.mahou.bootjava.restaurantvoting.MenuTestData.*;
import static com.mahou.bootjava.restaurantvoting.UserTestData.ADMIN_MAIL;
import static com.mahou.bootjava.restaurantvoting.UserTestData.USER_ID;
import static com.mahou.bootjava.restaurantvoting.util.JsonUtil.writeValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishControllerTest extends AbstractControllerTest {
    public static final String URL = AdminDishController.URL + '/';

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByMenuId() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "by-menu/" + EATALY_CURRENT_MENU_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1, dish2, dish3, dish4));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + EATALY_CURRENT_MENU_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertFalse(repository.findById(USER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = new Dish(null, "Mochaccino", new BigDecimal(190));
        newDish.setMenu(eataly_current_menu);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .param("menuId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newDish)));

        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.findById(newId).orElseThrow(), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = dish1;
        updated.setMenu(eataly_menu);
        updated.setTitle("Super Capricciosa");
        perform(MockMvcRequestBuilders.put(URL + DISH1_ID)
                .param("menuId", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(repository.findById(DISH1_ID).orElseThrow(), updated);
    }
}