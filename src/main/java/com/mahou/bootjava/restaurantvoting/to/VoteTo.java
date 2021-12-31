package com.mahou.bootjava.restaurantvoting.to;

import com.mahou.bootjava.restaurantvoting.HasId;
import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDate;

@Value
@ToString
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo implements HasId {

    @NotNull
    LocalDate date;

    @NotNull
    Integer restaurantId;

    public VoteTo(Integer id, LocalDate date, Integer restaurantId) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
    }
}
