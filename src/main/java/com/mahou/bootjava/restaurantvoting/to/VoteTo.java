package com.mahou.bootjava.restaurantvoting.to;

import com.mahou.bootjava.restaurantvoting.HasId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
public class VoteTo implements HasId {
    private Integer id;

    private LocalDate date;

    private Integer restaurantId;

    public VoteTo(Integer id, LocalDate date, Integer restaurantId) {
        this.id = id;
        this.date = date;
        this.restaurantId = restaurantId;
    }
}
