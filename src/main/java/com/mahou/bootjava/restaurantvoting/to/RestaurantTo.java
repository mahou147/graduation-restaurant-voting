package com.mahou.bootjava.restaurantvoting.to;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends BaseTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String title;

    private String address;

    private String phone;

    @ConstructorProperties({"id", "title", "address", "phone"})
    public RestaurantTo(Integer id, String title, String address, String phone) {
        super(id);
        this.title = title;
        this.address = address;
        this.phone = phone;
    }
}
