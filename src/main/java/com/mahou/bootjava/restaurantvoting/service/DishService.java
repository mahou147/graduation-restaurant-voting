package com.mahou.bootjava.restaurantvoting.service;

import com.mahou.bootjava.restaurantvoting.model.Dish;
import com.mahou.bootjava.restaurantvoting.repository.DishRepository;
import com.mahou.bootjava.restaurantvoting.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    private final MenuRepository menuRepository;

    @Transactional
    public Dish create(@RequestBody Dish dish, @RequestParam Integer menuId) {
        log.info("create for menu {}", menuId);
        checkNew(dish);
        Assert.notNull(dish, "Menu must not be null");
        setMenu(dish, menuId);
        return dishRepository.save(dish);
    }

    @Transactional
    public void update(@RequestBody Dish dish, @PathVariable int id, @RequestParam Integer menuId) {
        log.info("update Dish {}", id);
        assureIdConsistent(dish, id);
        Assert.notNull(dish, "Menu must not be null");
        setMenu(dish, menuId);
        checkNotFoundWithId(dishRepository.save(dish), id);
    }

    private void setMenu(Dish dish, Integer menuId) {
        dish.setMenu(checkNotFoundWithId(menuRepository.getById(menuId), menuId));
    }
}
