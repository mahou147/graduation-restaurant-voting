package com.mahou.bootjava.restaurantvoting.service;

import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.Dish;
import com.mahou.bootjava.restaurantvoting.model.Menu;
import com.mahou.bootjava.restaurantvoting.repository.DishRepository;
import com.mahou.bootjava.restaurantvoting.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final DishRepository dishRepository;

    public Menu getWithDishes(int id) {
        log.info("get {}", id);
        return menuRepository.getWithDishes(id).orElseThrow(() -> new NotFoundException("Menu not found, id: " + id));
    }

    @Cacheable("menus")
    public List<Menu> getAllWithDishesByRestaurantId(int id) {
        log.info("getAll");
        return menuRepository.findAllByRestaurantId(id);
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public void deleteById(int id) {
        log.info("delete {}", id);
        menuRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public Menu create(@RequestBody Menu menu) {
        log.info("create {}", menu);
        checkNew(menu);
        Assert.notNull(menu, "Menu must not be null");
        return menuRepository.save(menu);
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public void update(@RequestBody Menu menu, @PathVariable int id) {
        log.info("update menu {}", id);
        assureIdConsistent(menu, id);
        Assert.notNull(menu, "Menu must not be null");
        List<Dish> dishes = menu.getDishes();
        dishRepository.saveAll(dishes);
        checkNotFoundWithId(menuRepository.save(menu), id);
    }
}
