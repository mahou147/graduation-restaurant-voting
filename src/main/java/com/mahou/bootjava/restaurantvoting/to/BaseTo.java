package com.mahou.bootjava.restaurantvoting.to;

import com.mahou.bootjava.restaurantvoting.HasId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class BaseTo implements HasId {

    protected Integer id;

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }
}
