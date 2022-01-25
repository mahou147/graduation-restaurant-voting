package com.github.mahou147.voting.web.restaurant;

import com.github.mahou147.voting.UserTestData;
import com.github.mahou147.voting.model.Restaurant;
import com.github.mahou147.voting.util.JsonUtil;
import com.github.mahou147.voting.web.AbstractControllerTest;
import com.github.mahou147.voting.error.NotFoundException;
import com.github.mahou147.voting.repository.RestaurantRepository;
import com.github.mahou147.voting.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static com.github.mahou147.voting.MenuTestData.eataly_current_menu;
import static com.github.mahou147.voting.MenuTestData.ramen_current_menu;
import static com.github.mahou147.voting.RestaurantTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantControllerTest extends AbstractControllerTest {
    private static final String URL = AdminRestaurantController.URL + '/';

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private RestaurantService service;

    @BeforeTestClass
    public void before() {
        eataly.setMenus(Set.of(eataly_current_menu));
        ramen.setMenus(Set.of(ramen_current_menu));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + EATALY_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(eataly));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + EATALY_ID + "/menu-of-the-day"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJsonWithoutPageable(List.of(eataly, ramen)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getWithMenuOfTheDay() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + EATALY_ID + "/menu-of-the-day"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(eataly));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllWithMenuOfTheDay() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "/menu-of-the-day"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(eataly, ramen));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + EATALY_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(EATALY_ID));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "Forum Restaurant&Bar",
                "Novaya, 100, Moscow, Moscow Oblast, 14302", "79651507772");
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)));

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.findById(newId).orElseThrow(), newRestaurant);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = eataly;
        updated.setPhone("84957362891");
        perform(MockMvcRequestBuilders.put(URL + EATALY_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(repository.findById(EATALY_ID).orElseThrow(), updated);
    }
}