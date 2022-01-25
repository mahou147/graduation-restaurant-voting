package com.github.mahou147.voting.mappers;

import com.github.mahou147.voting.model.Restaurant;
import com.github.mahou147.voting.to.RestaurantTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    RestaurantTo toRestTo(Restaurant r);

    default Page<RestaurantTo> toRestTo(Page<Restaurant> page) {
        return page.map(this::toRestTo);
    }
}
